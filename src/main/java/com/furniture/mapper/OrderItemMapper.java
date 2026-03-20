package com.furniture.mapper;

import com.furniture.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    // 插入订单详情
    void insert(OrderItem orderItem);
    
    // 根据订单ID查询订单详情
    List<OrderItem> findByOrderId(@Param("orderId") String orderId);
    
    // 根据ID查询订单详情
    OrderItem findById(@Param("id") Integer id);
    
    // 根据订单ID删除订单详情
    void deleteByOrderId(@Param("orderId") String orderId);
}