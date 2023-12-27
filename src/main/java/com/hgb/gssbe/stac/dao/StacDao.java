package com.hgb.gssbe.stac.dao;

import com.hgb.gssbe.stac.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StacDao {

    List<StacMonth> selectStacProductList(StacReq stacReq);

    StacOrg selectStacOrg(StacReq stacReq);

    void modifyProduct(StacProductReq stacProductReq);
}
