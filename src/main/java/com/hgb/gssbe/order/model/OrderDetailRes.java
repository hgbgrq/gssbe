package com.hgb.gssbe.order.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderDetailRes {
    private String orderId;
    private String orgId;
    private String orgName;
    private String orderOrderingDate;
    private String orderDeadLineDate;
    private List<OrderProductRes> productList;
}
