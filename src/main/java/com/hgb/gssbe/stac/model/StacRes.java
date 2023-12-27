package com.hgb.gssbe.stac.model;

import com.hgb.gssbe.common.GssResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StacRes{

    String orgId;

    String orgName;

    List<StacMonth> stacMonthList;

}
