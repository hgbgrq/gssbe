package com.hgb.gssbe.stac.ctrl;

import com.hgb.gssbe.common.GssResponse;
import com.hgb.gssbe.stac.model.*;
import com.hgb.gssbe.stac.svc.StacSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stac")
@Tag(name = "매입장")
public class StacCtrl {

    @Autowired
    private StacSvc stacSvc;
    
    @Operation(summary = "매입장 조회")
    @GetMapping
    public ResponseEntity<StacRes> selectStacList(StacReq stacReq){
        StacRes result = stacSvc.selectStacList(stacReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GssResponse> modifyProduct(@RequestBody StacModifyReq stacModifyReq){
        stacSvc.modifyProduct(stacModifyReq);
        return new ResponseEntity<>(new GssResponse() ,HttpStatus.OK);
    }
}
