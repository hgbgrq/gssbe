package com.hgb.gssbe.order.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderModifyReq {

    private String orderId;
    private String orgId;
    private String orderOrderingDate;
    private String orderDeadLineDate;
    private String updateUserId;
    List<OrderProduct> productList;
}
