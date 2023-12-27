package com.hgb.gssbe.stac.model;

import lombok.Data;

import java.util.List;

@Data
public class StacMonth {

    private String month;

    private List<StacProduct> stacProductList;

}
