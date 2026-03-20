package com.furniture.service;

import com.furniture.entity.CartItem;
import com.furniture.entity.Order;
import java.util.List;

public interface OrderService {
    // 创建订单
    Order createOrder(int userId, List<CartItem> cartItems, String address, String paymentMethod);
    
    // 根据ID查询订单
    Order findById(String id);
    
    // 根据用户ID查询订单
    List<Order> findByUserId(int userId);
    
    // 查询所有订单
    List<Order> findAll();
    
    // 更新订单状态
    void updateStatus(String orderId, int status);
    
    // 更新订单
    void update(Order order);
}