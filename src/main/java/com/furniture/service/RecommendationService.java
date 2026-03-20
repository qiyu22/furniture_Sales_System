package com.furniture.service;

import com.furniture.entity.Product;
import java.util.List;

public interface RecommendationService {
    /**
     * 根据用户ID推荐产品
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐产品列表
     */
    List<Product> recommendByUserId(Integer userId, int limit);
    
    /**
     * 根据产品ID推荐相关产品
     * @param productId 产品ID
     * @param limit 推荐数量
     * @return 推荐产品列表
     */
    List<Product> recommendByProductId(Integer productId, int limit);
    
    /**
     * 记录用户浏览行为
     * @param userId 用户ID
     * @param productId 产品ID
     */
    void recordUserView(Integer userId, Integer productId);
    
    /**
     * 记录用户购买行为
     * @param userId 用户ID
     * @param productId 产品ID
     */
    void recordUserPurchase(Integer userId, Integer productId);
}