package com.hgb.gssbe.file.model;

import lombok.Data;

import java.util.List;

@Data
public class FileDetailRes {

    private String fileId;

    private String fileName;

    private String createDate;

    private String createUserId;

    private List<FileDetailOrderRes> list;


}
