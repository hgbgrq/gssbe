package com.hgb.gssbe.stac.dao;

import com.hgb.gssbe.stac.model.Stac;
import com.hgb.gssbe.stac.model.StacReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StacDao {

    List<Stac> selectStacList(StacReq stacReq);
}
