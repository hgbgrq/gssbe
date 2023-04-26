package com.hgb.gssbe.stac.svc;


import com.hgb.gssbe.stac.dao.StacDao;
import com.hgb.gssbe.stac.model.Stac;
import com.hgb.gssbe.stac.model.StacReq;
import com.hgb.gssbe.stac.model.StacRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StacSvc {

    @Autowired
    private StacDao stacDao;

    public StacRes selectStacList(StacReq stacReq){
        StacRes result = new StacRes();
        List<Stac> list =stacDao.selectStacList(stacReq);
        result.setStacList(list);
        return result;
    }


}

