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
    
    // 根据用户ID分页查询订单
    List<Order> findByUserIdWithPagination(int userId, int page, int pageSize);
    
    // 查询所有订单
    List<Order> findAll();
    
    // 分页查询所有订单
    List<Order> findAllWithPagination(int page, int pageSize, Integer status);

    int countAll(Integer status);

    int countByUserId(int userId);
    
    // 更新订单状态
    void updateStatus(String orderId, int status);
    
    // 更新订单
    void update(Order order);
    
    // 根据订单号查询订单
    Order findByOrderId(String orderId);
    
    // 自动取消超时未付款的订单
    int autoCancelTimeoutOrders(int hours);
}