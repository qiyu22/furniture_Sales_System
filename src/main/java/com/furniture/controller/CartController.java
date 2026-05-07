package com.furniture.controller;

import com.furniture.entity.CartItem;
import com.furniture.service.CartService;
import com.furniture.utils.JwtUtils;
import com.furniture.utils.Result;
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
    public Result getCartItems(@ApiParam("用户ID") @PathVariable int userId) {
        List<CartItem> cartItems = cartService.findByUserId(userId);
        return Result.success(cartItems);
    }
    
    @ApiOperation("添加购物车项")
    @PostMapping
    public Result addCartItem(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        if (cartRequest.getProductId() <= 0) {
            return Result.error("无效的商品ID");
        }
        if (cartRequest.getQuantity() <= 0) {
            return Result.error("数量必须大于0");
        }
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
                return Result.success("添加成功");
            }
        }
        
        return Result.error("未登录");
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
    public Result updateCartItem(@ApiParam("购物车项ID") @PathVariable int id, @ApiParam("数量") @RequestParam int quantity) {
        if (quantity <= 0) {
            return Result.error("数量必须大于0");
        }
        cartService.updateCartItem(id, quantity);
        return Result.success("更新成功");
    }
    
    @ApiOperation("删除购物车项")
    @DeleteMapping("/{id}")
    public Result deleteCartItem(@ApiParam("购物车项ID") @PathVariable int id) {
        cartService.deleteCartItem(id);
        return Result.success("删除成功");
    }
    
    @ApiOperation("清空购物车")
    @DeleteMapping("/user/{userId}")
    public Result clearCart(@ApiParam("用户ID") @PathVariable int userId) {
        cartService.clearCart(userId);
        return Result.success("清空成功");
    }
    
    @ApiOperation("获取购物车数量")
    @GetMapping("/count")
    public Result getCartCount(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 解析token获取用户ID
            Claims claims = jwtUtils.parseToken(token);
            Integer userId = ((Number) claims.get("userId")).intValue();
            if (userId != null) {
                int count = cartService.getCartCount(userId);
                return Result.success(count);
            }
        }
        return Result.success(0);
    }
}