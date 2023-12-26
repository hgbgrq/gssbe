package com.hgb.gssbe.order.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Data
public class OrderResList {

    private Integer totalCount;

    private List<OrderRes> list;


    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
