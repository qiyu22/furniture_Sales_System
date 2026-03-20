package com.furniture.mapper;

import com.furniture.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemMapper {
    // 根据用户ID查询购物车项
    List<CartItem> findByUserId(@Param("userId") Integer userId);
    
    // 根据用户ID和产品ID查询购物车项
    CartItem findByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    
    // 插入购物车项
    void insert(CartItem cartItem);
    
    // 更新购物车项数量
    void updateQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);
    
    // 删除购物车项
    void delete(@Param("id") Integer id);
    
    // 清空用户购物车
    void clearByUserId(@Param("userId") Integer userId);
    
    // 统计用户购物车数量
    int countByUserId(@Param("userId") Integer userId);
}