package com.hgb.gssbe.order.dao;


import com.hgb.gssbe.order.model.OrderModel;
import com.hgb.gssbe.order.model.OrderProductModel;
import com.hgb.gssbe.order.model.OrderReq;
import com.hgb.gssbe.order.model.OrderResDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDao {
    String test();

    List<OrderModel> selectOrders(OrderReq orderReq);

    Integer selectOrdersCount(OrderReq orderReq);

    OrderResDetail selectDetailOrder(String ordId);

    void insertOrder(OrderModel orderModel);

    void insertOrderProduct(OrderProductModel orderProductModel);

    void updateFileTmpYn(String fileId);

}
