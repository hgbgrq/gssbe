package com.hgb.gssbe.stac.model;

import lombok.Data;

@Data
public class StacProduct {

    private String productId;

    private String prdStyleNo;

    private String prdItem;

    private String prdSize;

    private String prdColor;

    private Integer prdQty;

    private Integer prdPrc;

    private Integer totalPrdPrc;

    private String prdEtc;

    private String isPayment;

    private String paymentDate;

    private String orgId;

    private String orgName;

}
