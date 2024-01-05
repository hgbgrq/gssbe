package com.hgb.gssbe.order.ctrl;


import com.hgb.gssbe.common.GssResponse;
import com.hgb.gssbe.order.model.*;
import com.hgb.gssbe.order.svc.OrderSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@Tag(name = "발주")
public class OrderCtrl {

    @Autowired
    private OrderSvc orderSvc;

    @Operation(summary = "발주 조회")
    @GetMapping
    public ResponseEntity<OrderResList> getOrderList(OrderReq orderReq){
        log.info(orderReq.toStringJson());
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
}
