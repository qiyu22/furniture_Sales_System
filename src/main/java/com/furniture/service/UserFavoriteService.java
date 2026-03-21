package com.furniture.service;

import java.util.List;

public interface UserFavoriteService {
    // 添加收藏
    void addFavorite(Integer userId, Integer productId);
    
    // 取消收藏
    void removeFavorite(Integer userId, Integer productId);
    
    // 检查是否已收藏
    boolean isFavorite(Integer userId, Integer productId);
    
    // 获取用户收藏的商品ID列表
    List<Integer> getFavoriteProductIds(Integer userId);
}