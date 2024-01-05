package com.hgb.gssbe.order.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderDeleteReqList {

    List<OrderDeleteReq> orderList;
}
