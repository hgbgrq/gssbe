package com.hgb.gssbe.stac.model;

import lombok.Data;

import java.util.List;

@Data
public class Stac {

    String orderMonth;

    Integer totalCount;

    List<StacProduct> productList;

    Integer totalPrice;
}
