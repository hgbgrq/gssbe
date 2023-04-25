package com.hgb.gssbe.stac.model;

import lombok.Data;

@Data
public class StacProduct {
    private String prdStyleNo;

    private String prdItem;

    private String prdSize;

    private String prdColor;

    private String prdQty;

    private String prdPrc;

    private String prdEtc;

    private String isPayment;

    private String createDate;
}
