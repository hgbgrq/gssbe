package com.hgb.gssbe.org.ctrl;

import com.hgb.gssbe.org.model.OrgDetailRes;
import com.hgb.gssbe.org.model.OrgReq;
import com.hgb.gssbe.org.model.OrgResList;
import com.hgb.gssbe.org.svc.OrgSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/org")
public class OrgCtrl {

    @Autowired
    private OrgSvc orgSvc;

    @GetMapping
    public ResponseEntity<OrgResList> getOrgList(OrgReq orgReq){
        OrgResList result = orgSvc.selectOrgList(orgReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{orgId}")
    public ResponseEntity<OrgDetailRes> getOrgDetail(@PathVariable String orgId){
        OrgDetailRes result = new OrgDetailRes();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createOrg(){

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
