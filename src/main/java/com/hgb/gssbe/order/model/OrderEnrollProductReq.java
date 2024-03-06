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

    private Integer productQty;

    private Integer productPrice;

    private String productEtc;

    private Integer totalPrdPrc;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
