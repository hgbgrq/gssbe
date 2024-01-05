package com.hgb.gssbe.stac.ctrl;

import com.hgb.gssbe.common.GssResponse;
import com.hgb.gssbe.stac.model.*;
import com.hgb.gssbe.stac.svc.StacSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/stac")
@Tag(name = "매입장")
public class StacCtrl {

    @Autowired
    private StacSvc stacSvc;
    
    @Operation(summary = "매입장 조회")
    @GetMapping
    public ResponseEntity<StacRes> selectStacList(StacReq stacReq){
        return new ResponseEntity<>(stacSvc.selectStacList(stacReq), HttpStatus.OK);
    }

    @Operation(summary = "단가 수정")
    @PostMapping
    public ResponseEntity<GssResponse> modifyProduct(@RequestBody StacModifyListReq stacModifyListReq){
        log.info(stacModifyListReq.toStringJson());
        stacSvc.modifyProduct(stacModifyListReq);
        return new ResponseEntity<>(new GssResponse() ,HttpStatus.OK);
    }
}
