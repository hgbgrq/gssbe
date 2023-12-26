package com.hgb.gssbe.order.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Data
public class OrderEnrollInfoReq {

    private String orderOrderingDate;

    private String orderDeadLineDate;

    private String orgId;

    private List<OrderEnrollProductReq> productList;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
