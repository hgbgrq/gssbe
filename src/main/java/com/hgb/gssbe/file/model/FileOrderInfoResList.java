package com.hgb.gssbe.file.model;

import lombok.Data;

import java.util.List;

@Data
public class FileOrderInfoResList {

    String fileId;

    List<FileOrderInfo> list;

}
