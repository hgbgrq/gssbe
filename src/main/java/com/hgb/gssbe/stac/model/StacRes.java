package com.hgb.gssbe.stac.model;

import com.hgb.gssbe.common.GssResponse;
import lombok.Data;

import java.util.List;

@Data
public class StacRes extends GssResponse {

    List<Stac> stacList;

}
