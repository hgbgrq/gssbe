package com.hgb.gssbe.stac.svc;


import com.hgb.gssbe.common.exception.GssException;
import com.hgb.gssbe.stac.dao.StacDao;
import com.hgb.gssbe.stac.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class StacSvc {

    @Autowired
    private StacDao stacDao;

    public StacRes selectStacList(StacReq stacReq) {
        StacOrg stacOrg = stacDao.selectStacOrg(stacReq);

        if (stacOrg == null) {
            return StacRes.builder().build();
        }
        List<StacMonth> stacMonthList = stacDao.selectStacProductList(stacReq);

        return StacRes.builder()
                .orgId(stacOrg.getOrgId())
                .orgName(stacOrg.getOrgName())
                .stacMonthList(stacMonthList).build();
    }

    public void modifyProduct(StacModifyListReq stacModifyListReq) {
        if (CollectionUtils.isEmpty(stacModifyListReq.getStacProductReqList())) {
            throw new GssException("");
        }
        for (StacModifyReq stacModifyReq : stacModifyListReq.getStacProductReqList()) {
            for (StacProductReq stacProductReq : stacModifyReq.getStacProductList()) {
                stacDao.modifyProduct(stacProductReq);
            }
        }
    }

}

