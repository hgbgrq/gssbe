package com.hgb.gssbe.org.ctrl;

import com.hgb.gssbe.org.model.OrgDetailRes;
import com.hgb.gssbe.org.model.OrgReq;
import com.hgb.gssbe.org.model.OrgResList;
import com.hgb.gssbe.org.svc.OrgSvc;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "조직 현황 조회")
    public ResponseEntity<OrgResList> getOrgList(OrgReq orgReq){
        OrgResList result = orgSvc.selectOrgList(orgReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{orgId}")
    @Operation(description = "조직 상세 조회")
    public ResponseEntity<OrgDetailRes> getOrgDetail(@PathVariable String orgId){
        OrgDetailRes result = new OrgDetailRes();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    @Operation(description = "조직 등록")
    public ResponseEntity<String> createOrg(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
