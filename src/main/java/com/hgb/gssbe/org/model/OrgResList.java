package com.hgb.gssbe.org.model;

import com.hgb.gssbe.common.GssResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public class OrgResList extends GssResponse {

    Integer totalCount;

    List<OrgRes> list;


}
