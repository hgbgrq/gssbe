package com.hgb.gssbe.file.model;

import lombok.Data;

import java.util.List;

@Data
public class FileResList {

    Integer totalCount;

    List<FileRes> list;
}
