package com.hgb.gssbe.file.ctrl;


import com.hgb.gssbe.file.model.*;
import com.hgb.gssbe.file.svc.FileSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@Tag(name = "발주서 관리")
public class FileCtrl {

    @Autowired
    private FileSvc fileSvc;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "파일 등록")
    public ResponseEntity<List<FileUploadRes>> uploadOrderExcel(@RequestPart(value="file") List<MultipartFile> files) throws Exception {

        String userId = "test";
        List<FileUploadRes> result = fileSvc.orderExcelUpload(files,userId);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping
    @Operation(description = "파일 목록 조회")
    public ResponseEntity<FileResList> getFiles(FileReq fileReq){
        FileResList result = fileSvc.selectFiles(fileReq);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("{fileId}")
    @Operation(description = "파일 상세정보")
    public ResponseEntity<FileDetailRes> getFileDetail(@PathVariable String fileId){
        FileDetailRes result = fileSvc.selectFileDetail(fileId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
