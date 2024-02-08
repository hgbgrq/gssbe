package com.hgb.gssbe.stac.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StacRes {

    String orgId;

    String orgName;

    List<StacMonth> stacMonthList;

}
