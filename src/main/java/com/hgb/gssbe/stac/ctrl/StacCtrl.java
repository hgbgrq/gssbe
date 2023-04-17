package com.hgb.gssbe.stac.ctrl;

import com.hgb.gssbe.stac.model.StacReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class StacCtrl {

    @GetMapping
    public ResponseEntity<String> selectStacList(StacReq stacReq){
        return new ResponseEntity<>("test", HttpStatus.OK);
    }





}
