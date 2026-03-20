package com.furniture.controller;

import com.furniture.entity.CartItem;
import com.furniture.entity.Order;
import com.furniture.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @ApiOperation("创建订单")
    @PostMapping
    public Order createOrder(@ApiParam("用户ID") @RequestParam int userId, 
                            @ApiParam("购物车项列表") @RequestBody List<CartItem> cartItems, 
                            @ApiParam("收货地址") @RequestParam String address, 
                            @ApiParam("支付方式") @RequestParam String paymentMethod) {
        return orderService.createOrder(userId, cartItems, address, paymentMethod);
    }
    
    @ApiOperation("根据ID查询订单")
    @GetMapping("/{id}")
    public Order getOrderById(@ApiParam("订单ID") @PathVariable String id) {
        return orderService.findById(id);
    }
    
    @ApiOperation("根据用户ID查询订单")
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@ApiParam("用户ID") @PathVariable int userId) {
        return orderService.findByUserId(userId);
    }
    
    @ApiOperation("获取所有订单")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }
    
    @ApiOperation("更新订单状态")
    @PutMapping("/{id}/status")
    public void updateOrderStatus(@ApiParam("订单ID") @PathVariable String id, @ApiParam("订单状态") @RequestParam int status) {
        orderService.updateStatus(id, status);
    }
    
    @ApiOperation("更新订单")
    @PutMapping("/{id}")
    public void updateOrder(@ApiParam("订单ID") @PathVariable String id, @ApiParam("订单信息") @RequestBody Order order) {
        order.setId(id);
        orderService.update(order);
    }
}