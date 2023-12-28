package com.hgb.gssbe.order.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class OrderReq {

    private String orgId;

    private String startDate;

    private String endDate;

    private Integer currentPage;

    private Integer pageSize;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
