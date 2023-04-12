package com.hgb.gssbe.file.model;

import lombok.Data;

import java.util.List;

@Data
public class FileOrderInfo {

    private List<FileOrganization> orgInfos;

    private String orderingDaTe;

    private String deadLineDate;

    private List<FileProduct> products;

}
