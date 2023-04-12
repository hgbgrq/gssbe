package com.hgb.gssbe.file.ctrl;


import com.hgb.gssbe.file.model.FileDetailRes;
import com.hgb.gssbe.file.model.FileOrderInfoResList;
import com.hgb.gssbe.file.model.FileResList;
import com.hgb.gssbe.file.svc.FileSvc;
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
    public ResponseEntity<List<FileOrderInfoResList>> uploadOrderExcel(@RequestPart(value="file") List<MultipartFile> files) throws Exception {

        List<FileOrderInfoResList> result = fileSvc.orderExcelUpload(files);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<FileResList> getFiles(){
        FileResList result = fileSvc.selectFiles();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("{fileId}")
    public ResponseEntity<FileDetailRes> getFileDetail(@PathVariable String fileId){
        FileDetailRes result = fileSvc.selectFileDetail(fileId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }



}
