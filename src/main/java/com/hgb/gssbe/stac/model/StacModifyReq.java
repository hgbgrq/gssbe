package com.hgb.gssbe.stac.model;

import lombok.Data;

import java.util.List;

@Data
public class StacModifyReq {

    List<StacProductReq> stacProductReqList;
}
