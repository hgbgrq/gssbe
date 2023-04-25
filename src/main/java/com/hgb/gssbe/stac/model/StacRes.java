package com.hgb.gssbe.stac.model;

import lombok.Data;

import java.util.List;

@Data
public class StacRes {

    Integer totalCount;

    List<StacProduct> productList;

    Integer totalPrice;
}
