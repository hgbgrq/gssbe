package com.hgb.gssbe.file.model;

import com.hgb.gssbe.common.GssRequest;
import lombok.Data;

@Data
public class FileReq extends GssRequest {

    private String keyWord;

}
