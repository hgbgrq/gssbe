package com.hgb.gssbe.order.dao;


import com.hgb.gssbe.order.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDao {
    String test();

    List<OrderRes> selectOrders(OrderReq orderReq);

    Integer selectOrdersCount(OrderReq orderReq);

    OrderDetail selectDetailOrder(String orderId);

    void insertOrdering(Order order);

    void insertOrderProduct(OrderProduct orderProduct);


}
