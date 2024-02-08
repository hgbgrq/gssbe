package com.hgb.gssbe.file.ctrl;


import com.hgb.gssbe.common.GssResponse;
import com.hgb.gssbe.common.constance.FilePath;
import com.hgb.gssbe.file.model.FileDetailRes;
import com.hgb.gssbe.file.model.FileReq;
import com.hgb.gssbe.file.model.FileResList;
import com.hgb.gssbe.file.model.FileUploadRes;
import com.hgb.gssbe.file.svc.FileSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@Tag(name = "발주서 관리")
public class FileCtrl {

    @Autowired
    private FileSvc fileSvc;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "파일 등록")
    public ResponseEntity<FileUploadRes> uploadOrderExcel(@RequestPart(value = "file") MultipartFile file) throws Exception {
        String userId = "test";
        FileUploadRes result = fileSvc.orderExcelUpload(file, userId);
        return new ResponseEntity<>(result, result.getHttpStatus());
    }

    @GetMapping
    @Operation(description = "파일 목록 조회")
    public ResponseEntity<FileResList> getFiles(FileReq fileReq) {
        fileReq.setRow(fileReq.getSize() * (fileReq.getRow() - 1));
        FileResList result = fileSvc.selectFiles(fileReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{fileId}")
    @Operation(description = "파일 상세정보")
    public ResponseEntity<FileDetailRes> getFileDetail(@PathVariable String fileId) {
        FileDetailRes result = fileSvc.selectFileDetail(fileId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Operation(description = "파일 삭제")
    public ResponseEntity<GssResponse> deleteFile(@RequestBody List<String> fileIds) {
        fileSvc.deleteFile(fileIds);
        return new ResponseEntity<>(new GssResponse(), HttpStatus.OK);
    }

    @GetMapping("/download/{fileId}")
    @Operation(description = "파일 다운로드")
    public void downloadFile(@PathVariable String fileId, HttpServletResponse response) throws IOException {
        String path = FilePath.TMP_PATH.getPath() + "\\" + fileId + ".xlsx";
        File file = new File(path);
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        FileInputStream fileInputStream = new FileInputStream(path); // 파일 읽어오기
        OutputStream out = response.getOutputStream();

        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = fileInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
