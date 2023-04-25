package com.hgb.gssbe.file.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FileUploadRes {

    private String fileId;

    private String fileName;

    private String resultCode;

    private String resultMessage;

    private HttpStatus httpStatus;
}
