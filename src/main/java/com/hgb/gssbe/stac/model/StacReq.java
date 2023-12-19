package com.hgb.gssbe.stac.model;

import lombok.Data;

import java.util.List;

@Data
public class StacReq {

    private List<String> orgIdList;

    private String startDate;

    private String endDate;

    private String isPayment;

}
