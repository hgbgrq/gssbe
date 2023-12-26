package com.hgb.gssbe.order.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class OrderEnrollProductReq {

    private String productStyleNo;

    private String productItem;

    private String productSize;

    private String productColor;

    private String productQty;

    private String productPrice;

    private String productEtc;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
