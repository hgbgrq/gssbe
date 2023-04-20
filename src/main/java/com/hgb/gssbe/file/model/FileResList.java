package com.hgb.gssbe.file.model;

import com.hgb.gssbe.common.GssResponse;
import lombok.Data;

import java.util.List;

@Data
public class FileResList extends GssResponse {

    Integer totalCount;

    List<FileRes> list;
}
