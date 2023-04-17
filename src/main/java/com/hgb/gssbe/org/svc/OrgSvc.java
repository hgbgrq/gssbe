package com.hgb.gssbe.org.svc;

import com.hgb.gssbe.common.exception.GssException;
import com.hgb.gssbe.org.dao.OrgDao;
import com.hgb.gssbe.org.model.Org;
import com.hgb.gssbe.org.model.OrgReq;
import com.hgb.gssbe.org.model.OrgRes;
import com.hgb.gssbe.org.model.OrgResList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrgSvc {

    @Autowired
    private OrgDao orgDao;

    public OrgResList selectOrgList(OrgReq orgReq){
        OrgResList result = new OrgResList();
        List<OrgRes> list = orgDao.selectOrgList(orgReq);
        Integer totalCount = orgDao.selectOrgCount(orgReq);
        result.setList(list);
        result.setTotalCount(totalCount);
        return result;
    }

    public void createOrg(Org org){

        if (orgDao.selectOrgCountByName(org.getOrgName()) > 0){
            throw new GssException("registered-org-name");
        }

        orgDao.createOrg(org);
    }

    public void deleteOrg(String orgId){
        orgDao.deleteOrg(orgId);
    }

}
