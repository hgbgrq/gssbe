package com.hgb.gssbe.org.ctrl;

import com.hgb.gssbe.common.GssResponse;
import com.hgb.gssbe.org.model.Org;
import com.hgb.gssbe.org.model.OrgReq;
import com.hgb.gssbe.org.model.OrgResList;
import com.hgb.gssbe.org.svc.OrgSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/org")
public class OrgCtrl {

    @Autowired
    private OrgSvc orgSvc;

    @GetMapping
    @Operation(description = "조직 현황 조회")
    @Parameters(
            {
                    @Parameter(name = "keyWord" , description = "키워드(이름,전화번호,팩스번호)" , example = "테스트1")
            }
    )
    public ResponseEntity<OrgResList> getOrgList(@Parameter(hidden = true) OrgReq orgReq){
        OrgResList result = orgSvc.selectOrgList(orgReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{orgId}")
    @Operation(description = "조직 상세 조회")
    public ResponseEntity<Org> getOrgDetail(@PathVariable String orgId){
        Org result = orgSvc.selectOrgDetail(orgId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    @Operation(description = "조직 등록")
    public ResponseEntity<GssResponse> createOrg(@RequestBody Org org){
        org.setUserId("test");
        orgSvc.createOrg(org);
        return new ResponseEntity<>(new GssResponse(), HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Operation(description = "조직 삭제")
    public ResponseEntity<GssResponse> deleteOrg(@RequestBody List<String> orgIds){
        orgSvc.deleteOrg(orgIds);
        return new ResponseEntity<>(new GssResponse(), HttpStatus.OK);
    }

    @GetMapping("/check/{orgName}")
    public ResponseEntity<Boolean> checkDuplicationName(@PathVariable String orgName){
        Boolean result = orgSvc.checkDuplicationName(orgName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/modify")
    @Operation(description = "조직 수정")
    public ResponseEntity<GssResponse> modifyOrg(@RequestBody Org org){
        org.setUserId("test");
        orgSvc.modifyOrg(org);
        return new ResponseEntity<>(new GssResponse(), HttpStatus.OK);
    }

}
