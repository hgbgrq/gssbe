package com.hgb.gssbe.order.model;

import lombok.Data;

@Data
public class OrderProductRes {
    private String orderId;

    private String productId;

    private String productStyleNo;

    private String productItem;

    private String productSize;

    private String productColor;

    private String productQty;

    private String productPrice;

    private String productEtc;

    private String createUserId;

    private Integer totalPrdPrc;

}
