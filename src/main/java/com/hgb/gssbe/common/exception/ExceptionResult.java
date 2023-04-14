package com.hgb.gssbe.common.exception;

import lombok.Data;

@Data
public class ExceptionResult {

    private String code;
    private String desc;
    private String status;
}
