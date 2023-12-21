package com.hgb.gssbe.order.model;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private String orderId;
    private String orgId;
    private String orgName;
    private String ordOrderingDate;
    private String ordFileId;
    private String ordDeadLineDate;
    private String ordFileName;
    private String ordName;
    private List<OrderProductModel> list;

}
