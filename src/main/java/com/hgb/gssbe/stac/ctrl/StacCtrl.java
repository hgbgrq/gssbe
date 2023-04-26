package com.hgb.gssbe.stac.ctrl;

import com.hgb.gssbe.stac.model.StacReq;
import com.hgb.gssbe.stac.model.Stac;
import com.hgb.gssbe.stac.model.StacRes;
import com.hgb.gssbe.stac.svc.StacSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stac")
public class StacCtrl {
    @Autowired
    private StacSvc stacSvc;
    @GetMapping
    public ResponseEntity<StacRes> selectStacList(StacReq stacReq){
        StacRes result = stacSvc.selectStacList(stacReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
