package com.hgb.gssbe.order.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetail {
    private String orderId;
    private String orgId;
    private String orgName;
    private String orderOrderingDate;
    private String orderDeadLineDate;
    private String createUserId;
    private List<OrderProductRes> productList;
}
