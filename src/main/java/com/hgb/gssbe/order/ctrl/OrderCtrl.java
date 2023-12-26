package com.hgb.gssbe.order.ctrl;


import com.hgb.gssbe.order.model.*;
import com.hgb.gssbe.order.svc.OrderSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/order")
@Tag(name = "발주")
public class OrderCtrl {

    @Autowired
    private OrderSvc orderSvc;

    @Operation(summary = "발주 조회")
    @GetMapping
    public ResponseEntity<OrderResList> getOrderList(OrderReq orderReq){
        OrderResList result = orderSvc.selectOrderList(orderReq);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @Operation(summary = "발주 등록")
    @PostMapping
    public ResponseEntity<OrderEnrollInfoRes> enrollOrder(@RequestBody OrderEnrollInfoReq orderEnrollInfoReq){
        String orderId = orderSvc.enrollOrder(orderEnrollInfoReq);
        OrderEnrollInfoRes res = new OrderEnrollInfoRes();
        res.setOrderId(orderId);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }


    @Operation(summary = "발주서 다운로드")
    @GetMapping("/downloadExcel/{orderId}")
    public void downloadOrder(HttpServletResponse response , @PathVariable String orderId){
        orderSvc.downloadOrder(response, orderId);
    }
}
