package com.furniture.service;

import com.furniture.entity.CartItem;
import java.util.List;

public interface CartService {
    // 根据用户ID查询购物车项
    List<CartItem> findByUserId(int userId);
    
    // 添加购物车项
    void addCartItem(int userId, int productId, int quantity);
    
    // 更新购物车项数量
    void updateCartItem(int id, int quantity);
    
    // 删除购物车项
    void deleteCartItem(int id);
    
    // 清空购物车
    void clearCart(int userId);
    
    int countByUserId(int userId);
    
    int getCartCount(int userId);
}