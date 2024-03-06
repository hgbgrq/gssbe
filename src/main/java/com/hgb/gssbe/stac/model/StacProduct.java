package com.hgb.gssbe.stac.model;

import lombok.Data;

@Data
public class StacProduct {

    private String orderId;

    private String productId;

    private String productStyleNo;

    private String productItem;

    private String productSize;

    private String productColor;

    private Integer productQty;

    private Integer productPrice;

    private Integer totalPrdPrc;

    private String productEtc;

    private String orderOrderingDate;

    private String orderDeadLineDate;

    private Integer taxPrice;

}
