package com.hgb.gssbe.file.model;

import lombok.Data;

import java.util.List;

@Data
public class FileOrderInfo {

    private String orderId;

    private String fileId;

    private String orgId;

    private String orgName;

    private String orderingDaTe;

    private String deadLineDate;

    private List<FileProduct> products;

    private String userId;

}
