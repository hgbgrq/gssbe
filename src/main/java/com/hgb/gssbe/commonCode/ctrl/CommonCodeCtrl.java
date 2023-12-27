package com.hgb.gssbe.commonCode.ctrl;

import com.hgb.gssbe.commonCode.model.CommonCodeRes;
import com.hgb.gssbe.commonCode.svc.CommonCodeSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common-code")
public class CommonCodeCtrl {

    @Autowired
    private CommonCodeSvc commonCodeSvc;

    @GetMapping("/{commonGroupId}")
    public ResponseEntity<CommonCodeRes> getCommonCode (@PathVariable String commonGroupId){
        CommonCodeRes commonCodeRes = commonCodeSvc.getCommonCode(commonGroupId);
        return new ResponseEntity<>(commonCodeRes , HttpStatus.OK);
    }


}
