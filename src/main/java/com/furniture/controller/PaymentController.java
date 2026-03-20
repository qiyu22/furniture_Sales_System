package com.furniture.controller;

import com.furniture.entity.Order;
import com.furniture.service.OrderService;
import com.furniture.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Map<String, Object> pay(
            @ApiParam("订单ID") @RequestParam String orderId,
            @ApiParam("支付金额") @RequestParam String amount,
            @ApiParam("支付方式") @RequestParam String paymentMethod,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
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
                        result.put("success", false);
                        result.put("message", "订单不存在");
                        return result;
                    }
                    
                    if (order.getUserId() != userId) {
                        result.put("success", false);
                        result.put("message", "订单不属于当前用户");
                        return result;
                    }
                    
                    // 检查订单状态
                    if (order.getStatus() != 0) { // 0: 待支付
                        result.put("success", false);
                        result.put("message", "订单状态不正确");
                        return result;
                    }
                    
                    // 验证金额
                    if (!order.getTotalPrice().toString().equals(amount)) {
                        result.put("success", false);
                        result.put("message", "金额不匹配");
                        return result;
                    }
                    
                    // 模拟支付过程
                    // 实际项目中这里应该调用第三方支付平台API
                    System.out.println("支付请求: 订单ID=" + orderId + ", 金额=" + amount + ", 支付方式=" + paymentMethod);
                    
                    // 支付成功后更新订单状态
                    order.setStatus(1); // 1: 已支付
                    order.setPaymentMethod(paymentMethod);
                    orderService.update(order);
                    
                    result.put("success", true);
                    result.put("message", "支付成功");
                    return result;
                }
            }
            
            result.put("success", false);
            result.put("message", "未登录或登录已过期");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "支付失败: " + e.getMessage());
            return result;
        }
    }
    
    @ApiOperation("获取支付状态")
    @GetMapping("/status/{orderId}")
    public Map<String, Object> getPaymentStatus(
            @ApiParam("订单ID") @PathVariable String orderId,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
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
                        result.put("success", false);
                        result.put("message", "订单不存在");
                        return result;
                    }
                    
                    if (order.getUserId() != userId) {
                        result.put("success", false);
                        result.put("message", "订单不属于当前用户");
                        return result;
                    }
                    
                    result.put("success", true);
                    result.put("status", order.getStatus());
                    result.put("paymentMethod", order.getPaymentMethod());
                    return result;
                }
            }
            
            result.put("success", false);
            result.put("message", "未登录或登录已过期");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取支付状态失败: " + e.getMessage());
            return result;
        }
    }
}