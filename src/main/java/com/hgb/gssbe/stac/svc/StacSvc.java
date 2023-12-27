package com.hgb.gssbe.stac.svc;


import com.hgb.gssbe.stac.dao.StacDao;
import com.hgb.gssbe.stac.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StacSvc {

    @Autowired
    private StacDao stacDao;

    public StacRes selectStacList(StacReq stacReq){
        StacOrg stacOrg = stacDao.selectStacOrg(stacReq);
        List<StacMonth> stacMonthList = stacDao.selectStacProductList(stacReq);

        return StacRes.builder()
                .orgId(stacOrg.getOrgId())
                .orgName(stacOrg.getOrgName())
                .stacMonthList(stacMonthList).build();
    }

    public void modifyProduct(StacModifyReq stacModifyReq){
        for(StacProductReq stacProductReq: stacModifyReq.getStacProductReqList()){
            stacDao.modifyProduct(stacProductReq);
        }
    }

}

