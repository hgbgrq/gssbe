package com.hgb.gssbe.order.ctrl;


import com.hgb.gssbe.order.model.OrderEnrollReq;
import com.hgb.gssbe.order.model.OrderReq;
import com.hgb.gssbe.order.model.Order;
import com.hgb.gssbe.order.model.OrderResList;
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
@RequestMapping("/order")
@Tag(name = "발주")
public class OrderCtrl {

    @Autowired
    private OrderSvc orderSvc;
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>(orderSvc.test(),HttpStatus.OK);
    }

    @Operation(summary = "발주 조회")
    @GetMapping
    public ResponseEntity<OrderResList> getOrderList(OrderReq orderReq){
        OrderResList result = orderSvc.selectOrderList(orderReq);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/{ordId}")
    public ResponseEntity<Order> getOrderDetail(@PathVariable String ordId){
        Order result = orderSvc.selectOrderDetail(ordId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> enrollOrder(@RequestBody List<OrderEnrollReq> orderEnrollReqs){
        orderSvc.insertOrderInfo(orderEnrollReqs);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/modify")
    public ResponseEntity<Void> modifyOrder(Order order){
        orderSvc.modifyOrder(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/ordering")
    public ResponseEntity<String> ordering(@RequestBody Order order){
        return new ResponseEntity<>("fileId",HttpStatus.OK);
    }

    @GetMapping("/downloadExcel/{fileId}")
    public void downloadOrder(HttpServletResponse response , @PathVariable String fileId){

    }
}
