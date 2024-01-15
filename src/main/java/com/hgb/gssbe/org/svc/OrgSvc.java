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
import java.util.UUID;

@Slf4j
@Service
public class OrgSvc {

    @Autowired
    private OrgDao orgDao;

    public OrgResList selectOrgList(OrgReq orgReq){
        return OrgResList.builder()
                        .list(orgDao.selectOrgList(orgReq))
                        .totalCount(orgDao.selectOrgCount(orgReq)).build();
    }

    public void createOrg(Org org){
        if (orgDao.selectOrgCountByName(org.getOrgName()) > 0){
            throw new GssException("registered-org-name");
        }
        org.setOrgId(UUID.randomUUID().toString());
        orgDao.createOrg(org);
    }

    public void deleteOrg(List<String> orgIds){
        for(String orgId : orgIds){
            orgDao.deleteOrg(orgId);
        }
    }

    public Org selectOrgDetail(String orgId){
        Org result = orgDao.selectOrgDetail(orgId);
        if(result == null){
            throw new GssException("");
        }
        return result;
    }

    public Boolean checkDuplicationName(String orgName){
        return orgDao.selectOrgCountByName(orgName) > 0;
    }

    public void modifyOrg(Org org){
        orgDao.modifyOrg(org);
    }

}
