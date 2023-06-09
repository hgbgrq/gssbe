package com.hgb.gssbe.org.dao;

import com.hgb.gssbe.org.model.Org;
import com.hgb.gssbe.org.model.OrgReq;
import com.hgb.gssbe.org.model.OrgRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrgDao {
    List<OrgRes> selectOrgList(OrgReq orgReq);

    Integer selectOrgCount(OrgReq orgReq);

    void createOrg(Org org);

    Integer selectOrgCountByName(String orgName);

    void deleteOrg(String orgId);

    Org selectOrgDetail(String orgId);

    void modifyOrg(Org org);
}
