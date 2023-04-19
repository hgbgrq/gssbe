package com.hgb.gssbe.order.svc;

import com.hgb.gssbe.order.dao.OrderDao;
import com.hgb.gssbe.order.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderSvc {

    @Autowired
    private OrderDao orderDao;

    public String test(){
        return orderDao.test();
    }

    public OrderResList selectOrderList(OrderReq orderReq){
        OrderResList result = new OrderResList();
        List<OrderModel> list = orderDao.selectOrders(orderReq);
        Integer totalCount = orderDao.selectOrdersCount(orderReq);
        result.setList(list);
        result.setTotalCount(totalCount);
        return result;
    }

    public OrderResDetail selectOrderDetail(String orderId){
        OrderResDetail result = orderDao.selectDetailOrder(orderId);
        return result;
    }

    public void insertOrderInfo(List<OrderEnrollReq> OrderEnrolls){
        for(OrderEnrollReq dto : OrderEnrolls){
            List<OrderEnrollInfoReq> orders  = dto.getList();

            for(OrderEnrollInfoReq order : orders){
                OrderModel orderModel = new OrderModel();
                String ordId = UUID.randomUUID().toString();
                orderModel.setFileId(dto.getFileId());
                orderModel.setOrdOrderingDate(order.getOrderingDate());
                orderModel.setOrdId(ordId);
                orderModel.setOrgId(order.getOrgId());
                orderModel.setDeadLineDate(order.getDeadLineDate());
                orderDao.insertOrder(orderModel);

                List<OrderEnrollProductReq> products = order.getProductList();
                for(OrderEnrollProductReq product: products){
                    OrderProductModel orderProductModel = new OrderProductModel();
                    String prdId = UUID.randomUUID().toString();
                    orderProductModel.setPrdId(prdId);
                    orderProductModel.setOrdId(ordId);
                    orderProductModel.setPrdStyleNo(product.getStyleNo());
                    orderProductModel.setPrdItem(product.getItem());
                    orderProductModel.setPrdSize(product.getSize());
                    orderProductModel.setPrdColor(product.getColor());
                    orderProductModel.setPrdQty(product.getQty());
                    orderProductModel.setPrdPrc(product.getPrc());
                    orderProductModel.setPrdEtc(product.getEtc());
                    orderDao.insertOrderProduct(orderProductModel);
                }

            }
            orderDao.updateFileTmpYn(dto.getFileId());
        }
    }
}
