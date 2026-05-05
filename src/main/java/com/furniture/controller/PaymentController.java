package com.furniture.controller;

import com.furniture.entity.Order;
import com.furniture.service.OrderService;
import com.furniture.utils.JwtUtils;
import com.furniture.utils.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "支付管理")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @ApiOperation("创建支付")
    @PostMapping("/pay")
    public Result pay(
            @ApiParam("订单ID") @RequestParam String orderId,
            @ApiParam("支付金额") @RequestParam String amount,
            @ApiParam("支付方式") @RequestParam String paymentMethod,
            HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                // 解析token获取用户ID
                Claims claims = jwtUtils.parseToken(token);
                Integer userId = ((Number) claims.get("userId")).intValue();
                
                if (userId != null) {
                    // 验证订单是否存在且属于当前用户
                    Order order = orderService.findByOrderId(orderId);
                    if (order == null) {
                        return Result.error("订单不存在");
                    }
                    
                    if (order.getUserId() != userId) {
                        return Result.error("订单不属于当前用户");
                    }
                    
                    // 检查订单状态
                    if (order.getStatus() != 0) { // 0: 待支付
                        return Result.error("订单状态不正确");
                    }
                    
                    // 验证金额
                    try {
                        BigDecimal paymentAmount = new BigDecimal(amount);
                        if (order.getTotalPrice().compareTo(paymentAmount) != 0) {
                            return Result.error("金额不匹配");
                        }
                    } catch (NumberFormatException e) {
                        return Result.error("金额格式错误");
                    }
                    
                    // 模拟支付过程
                    // 实际项目中这里应该调用第三方支付平台API
                    System.out.println("支付请求: 订单ID=" + orderId + ", 金额=" + amount + ", 支付方式=" + paymentMethod);
                    
                    // 支付成功后更新订单状态
                    order.setStatus(1);
                    order.setPaymentMethod(paymentMethod);
                    order.setPaymentTime(new Date());
                    orderService.update(order);
                    
                    return Result.success("支付成功");
                }
            }
            
            return Result.error("未登录或登录已过期");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("支付失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("获取支付状态")
    @GetMapping("/status/{orderId}")
    public Result getPaymentStatus(
            @ApiParam("订单ID") @PathVariable String orderId,
            HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                // 解析token获取用户ID
                Claims claims = jwtUtils.parseToken(token);
                Integer userId = ((Number) claims.get("userId")).intValue();
                
                if (userId != null) {
                    // 验证订单是否存在且属于当前用户
                    Order order = orderService.findByOrderId(orderId);
                    if (order == null) {
                        return Result.error("订单不存在");
                    }
                    
                    if (order.getUserId() != userId) {
                        return Result.error("订单不属于当前用户");
                    }
                    
                    Map<String, Object> data = new HashMap<>();
                    data.put("status", order.getStatus());
                    data.put("paymentMethod", order.getPaymentMethod());
                    return Result.success(data);
                }
            }
            
            return Result.error("未登录或登录已过期");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取支付状态失败: " + e.getMessage());
        }
    }
}