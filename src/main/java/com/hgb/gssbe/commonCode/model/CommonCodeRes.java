package com.hgb.gssbe.commonCode.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommonCodeRes {

    private List<CommonCode> commonCodeList;
}
