package com.hgb.gssbe.order.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class OrderModel {

    private String ordId;
    private String orgId;
    private String orgName;
    private String ordOrderingDate;
    private String deadLineDate;
    private String fileId;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
