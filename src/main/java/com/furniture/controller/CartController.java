package com.furniture.controller;

import com.furniture.entity.CartItem;
import com.furniture.service.CartService;
import com.furniture.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "购物车管理")
@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @ApiOperation("获取用户购物车")
    @GetMapping("/user/{userId}")
    public List<CartItem> getCartItems(@ApiParam("用户ID") @PathVariable int userId) {
        return cartService.findByUserId(userId);
    }
    
    @ApiOperation("添加购物车项")
    @PostMapping
    public void addCartItem(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            
            Claims claims = jwtUtils.parseToken(token);
            // 安全的类型转换，避免ClassCastException
            Integer userId = ((Number) claims.get("userId")).intValue();
            
            if (userId != null) {
                System.out.println("用户ID: " + userId);
                System.out.println("商品ID: " + cartRequest.getProductId());
                System.out.println("数量: " + cartRequest.getQuantity());
                
                cartService.addCartItem(
                        userId,
                        cartRequest.getProductId(),
                        cartRequest.getQuantity()
                );
                
                System.out.println("添加购物车请求处理完成");
                return;
            }
        }
        
        throw new RuntimeException("未登录");
    }
    
    // 内部静态类用于接收请求体参数
    static class CartRequest {
        private int productId;
        private int quantity;
        
        public int getProductId() {
            return productId;
        }
        
        public void setProductId(int productId) {
            this.productId = productId;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
    
    @ApiOperation("更新购物车项数量")
    @PutMapping("/{id}")
    public void updateCartItem(@ApiParam("购物车项ID") @PathVariable int id, @ApiParam("数量") @RequestParam int quantity) {
        cartService.updateCartItem(id, quantity);
    }
    
    @ApiOperation("删除购物车项")
    @DeleteMapping("/{id}")
    public void deleteCartItem(@ApiParam("购物车项ID") @PathVariable int id) {
        cartService.deleteCartItem(id);
    }
    
    @ApiOperation("清空购物车")
    @DeleteMapping("/user/{userId}")
    public void clearCart(@ApiParam("用户ID") @PathVariable int userId) {
        cartService.clearCart(userId);
    }
    
    @ApiOperation("获取购物车数量")
    @GetMapping("/count")
    public int getCartCount(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 解析token获取用户ID
            Claims claims = jwtUtils.parseToken(token);
            Integer userId = (Integer) claims.get("userId");
            if (userId != null) {
                return cartService.getCartCount(userId);
            }
        }
        return 0;
    }
}