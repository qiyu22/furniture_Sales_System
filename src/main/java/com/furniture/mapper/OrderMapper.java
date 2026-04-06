package com.furniture.mapper;

import com.furniture.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    // 插入订单
    void insert(Order order);
    
    // 根据ID查询订单
    Order findById(@Param("id") String id);
    
    // 根据用户ID查询订单
    List<Order> findByUserId(@Param("userId") Integer userId);
    
    // 查询所有订单
    List<Order> findAll();
    
    // 更新订单状态
    void updateStatus(@Param("orderId") String orderId, @Param("status") Integer status);
    
    // 更新订单
    void update(Order order);
    
    // 查询超时未付款的订单
    List<Order> findTimeoutOrders(@Param("status") Integer status, @Param("timeoutTime") java.util.Date timeoutTime);
}