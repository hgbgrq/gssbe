package com.hgb.gssbe.order.model;

import lombok.Data;

@Data
public class OrderProductModel {

    private String prdId;
    private String ordId;
    private String prdStyleNo;
    private String prdItem;
    private String prdSize;
    private String prdColor;
    private String prdQty;
    private String prdPrc;
    private String prdEtc;

}
