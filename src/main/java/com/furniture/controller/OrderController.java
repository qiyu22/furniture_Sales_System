package com.furniture.controller;

import com.furniture.entity.CartItem;
import com.furniture.entity.Order;
import com.furniture.service.OrderService;
import com.furniture.utils.Result;
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
    public Result createOrder(@ApiParam("用户ID") @RequestParam int userId, 
                            @ApiParam("购物车项列表") @RequestBody List<CartItem> cartItems, 
                            @ApiParam("收货地址") @RequestParam String address, 
                            @ApiParam("支付方式") @RequestParam String paymentMethod) {
        Order order = orderService.createOrder(userId, cartItems, address, paymentMethod);
        return Result.success(order);
    }
    
    @ApiOperation("根据ID查询订单")
    @GetMapping("/{id}")
    public Result getOrderById(@ApiParam("订单ID") @PathVariable String id) {
        Order order = orderService.findById(id);
        return Result.success(order);
    }
    
    @ApiOperation("根据用户ID查询订单")
    @GetMapping("/user/{userId}")
    public Result getOrdersByUserId(@ApiParam("用户ID") @PathVariable int userId) {
        List<Order> orders = orderService.findByUserId(userId);
        return Result.success(orders);
    }
    
    @ApiOperation("根据用户ID分页查询订单")
    @GetMapping("/user/{userId}/page")
    public Result getOrdersByUserIdWithPagination(@ApiParam("用户ID") @PathVariable int userId, @ApiParam("页码") @RequestParam int page, @ApiParam("每页大小") @RequestParam int pageSize) {
        List<Order> orders = orderService.findByUserIdWithPagination(userId, page, pageSize);
        int total = orderService.countByUserId(userId);
        return Result.page(orders, total);
    }
    
    @ApiOperation("获取所有订单")
    @GetMapping
    public Result getAllOrders() {
        List<Order> orders = orderService.findAll();
        return Result.success(orders);
    }
    
    @ApiOperation("分页获取所有订单")
    @GetMapping("/page")
    public Result getOrdersWithPagination(@ApiParam("页码") @RequestParam int page, @ApiParam("每页大小") @RequestParam int pageSize, @ApiParam("状态") @RequestParam(required = false) Integer status) {
        List<Order> orders = orderService.findAllWithPagination(page, pageSize, status);
        int total = orderService.countAll(status);
        return Result.page(orders, total);
    }
    
    @ApiOperation("更新订单状态")
    @PutMapping("/{id}/status")
    public Result updateOrderStatus(@ApiParam("订单ID") @PathVariable String id, @ApiParam("订单状态") @RequestParam int status) {
        orderService.updateStatus(id, status);
        return Result.success("更新成功");
    }
    
    @ApiOperation("更新订单")
    @PutMapping("/{id}")
    public Result updateOrder(@ApiParam("订单ID") @PathVariable String id, @ApiParam("订单信息") @RequestBody Order order) {
        order.setId(id);
        orderService.update(order);
        return Result.success("更新成功");
    }
}