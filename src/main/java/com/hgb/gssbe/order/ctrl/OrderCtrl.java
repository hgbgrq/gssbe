package com.hgb.gssbe.order.ctrl;


import com.hgb.gssbe.common.GssResponse;
import com.hgb.gssbe.file.model.FileUploadRes;
import com.hgb.gssbe.order.model.*;
import com.hgb.gssbe.order.svc.OrderSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Tag(name = "발주")
public class OrderCtrl {

    private final OrderSvc orderSvc;

    @Operation(summary = "발주 조회")
    @GetMapping
    public ResponseEntity<OrderResList> getOrderList(OrderReq orderReq){
        return new ResponseEntity<>(orderSvc.selectOrderList(orderReq) ,HttpStatus.OK);
    }


    @Operation(summary = "발주 등록")
    @PostMapping
    public ResponseEntity<OrderEnrollInfoRes> enrollOrder(@RequestBody OrderEnrollInfoReq orderEnrollInfoReq){
        return new ResponseEntity<>(orderSvc.enrollOrder(orderEnrollInfoReq),HttpStatus.OK);
    }


    @Operation(summary = "발주서 다운로드")
    @GetMapping("/downloadExcel/{orderId}")
    public void downloadOrder(HttpServletResponse response , @PathVariable String orderId){
        orderSvc.downloadOrder(response, orderId);
    }

    @Operation(summary = "발주서 삭제")
    @PostMapping("/remove")
    public ResponseEntity<GssResponse> deleteOrder(@RequestBody OrderDeleteReqList orderDeleteReqList){
        orderSvc.deleteOrder(orderDeleteReqList);
        return new ResponseEntity<>(new GssResponse(), HttpStatus.OK);
    }

    @Operation(summary = "발주서 상세조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailRes> getDetailOrder(@PathVariable String orderId){
        return new ResponseEntity<>(orderSvc.getDetailOrder(orderId), HttpStatus.OK);
    }

    @Operation(summary = "발주서 수정")
    @PostMapping("/modify")
    public ResponseEntity<GssResponse> modifyOrder(@RequestBody OrderModifyReq orderModifyReq){
        orderSvc.modifyOrder(orderModifyReq);
        return new ResponseEntity<>(new GssResponse(), HttpStatus.OK);
    }

    @Operation(summary = "발주서 등록")
    @PostMapping(value = "/uploadExcel" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GssResponse> uploadOrderExcel(@RequestPart(value="file") List<MultipartFile> orders) throws IOException, InvalidFormatException {
        orderSvc.uploadOrderExcel(orders);
        return new ResponseEntity<>(new GssResponse(), HttpStatus.OK);
    }
}
