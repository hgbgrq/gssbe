package com.hgb.gssbe.org.model;

import com.hgb.gssbe.common.GssResponse;
import lombok.Data;

@Data
public class Org extends GssResponse {

    private String orgId;

    private String orgName;

    private String orgAddress;

    private String orgNumber;

    private String orgPaxNumber;

    private String orgEmail;

    private String orgAddressDetail;

    private String orgZoneCode;

    private String orgEtc;

    private String userId;

}
