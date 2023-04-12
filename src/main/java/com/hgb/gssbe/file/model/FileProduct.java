package com.hgb.gssbe.file.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class FileProduct {

    private String styleNo;

    private String item;

    private String size;

    private String color;

    private String qty;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
