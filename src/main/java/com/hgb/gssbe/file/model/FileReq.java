package com.hgb.gssbe.file.model;

import com.hgb.gssbe.common.GssRequest;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class FileReq extends GssRequest {

    private String keyWord;

    private String keyWordType;

    private String startDate;

    private String endDate;

    public String toStringJson() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
