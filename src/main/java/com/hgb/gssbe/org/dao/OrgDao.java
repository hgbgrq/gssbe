package com.hgb.gssbe.org.dao;

import com.hgb.gssbe.org.model.OrgReq;
import com.hgb.gssbe.org.model.OrgRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrgDao {
    List<OrgRes> selectOrgList(OrgReq orgReq);

    Integer selectOrgCount(OrgReq orgReq);
}
