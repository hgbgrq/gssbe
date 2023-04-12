package com.hgb.gssbe.org.svc;

import com.hgb.gssbe.org.dao.OrgDao;
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
        log.info("조직 검색 시작");

        OrgResList result = new OrgResList();

        List<OrgRes> list = orgDao.selectOrgList(orgReq);

        Integer totalCount = orgDao.selectOrgCount(orgReq);

        result.setList(list);
        result.setTotalCount(totalCount);

        return result;

    }

}
