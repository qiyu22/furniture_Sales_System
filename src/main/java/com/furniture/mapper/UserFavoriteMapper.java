package com.furniture.mapper;

import java.util.List;

public interface UserFavoriteMapper {
    // 添加收藏
    void addFavorite(Integer userId, Integer productId);
    
    // 取消收藏
    void removeFavorite(Integer userId, Integer productId);
    
    // 检查是否已收藏
    Integer checkFavorite(Integer userId, Integer productId);
    
    // 获取用户收藏列表
    List<Integer> getFavoriteProductIds(Integer userId);
}