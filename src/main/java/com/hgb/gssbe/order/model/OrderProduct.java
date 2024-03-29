package com.hgb.gssbe.order.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Data
public class OrderProduct {

    private String orderId;

    private String productId;

    private String productStyleNo;

    private String productItem;

    private String productSize;

    private String productColor;

    private Integer productQty;

    private Integer productPrice;

    private Integer totalPrdPrc;

    private String productEtc;

    private String createUserId;

    private Integer productSort;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
