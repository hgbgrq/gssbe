package com.hgb.gssbe.org.model;

import com.hgb.gssbe.common.GssResponse;
import lombok.Data;

import java.util.List;

@Data
public class OrgResList extends GssResponse {


    Integer totalCount;

    List<OrgRes> list;


}
