package com.furniture.service.impl;

import com.furniture.entity.Product;
import com.furniture.service.RecommendationService;
import com.furniture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    
    @Autowired
    private ProductService productService;
    
    // 使用内存存储替代Redis
    private Map<String, Set<Integer>> memoryStorage = new HashMap<>();
    
    private static final String USER_VIEW_KEY = "user:view:";
    private static final String USER_PURCHASE_KEY = "user:purchase:";
    private static final String PRODUCT_SIMILARITY_KEY = "product:similarity:";
    
    @Override
    public List<Product> recommendByUserId(Integer userId, int limit) {
        // 获取用户浏览历史
        Set<Integer> viewedProductIds = memoryStorage.get(USER_VIEW_KEY + userId);
        // 获取用户购买历史
        Set<Integer> purchasedProductIds = memoryStorage.get(USER_PURCHASE_KEY + userId);
        
        // 合并历史记录
        Set<Integer> historyProductIds = new HashSet<>();
        if (viewedProductIds != null) {
            historyProductIds.addAll(viewedProductIds);
        }
        if (purchasedProductIds != null) {
            historyProductIds.addAll(purchasedProductIds);
        }
        
        // 基于历史记录推荐
        List<Product> recommendedProducts = new ArrayList<>();
        if (!historyProductIds.isEmpty()) {
            // 查找相似产品
            for (Integer productId : historyProductIds) {
                Set<Integer> similarProductIds = memoryStorage.get(PRODUCT_SIMILARITY_KEY + productId);
                if (similarProductIds != null) {
                    for (Integer similarId : similarProductIds) {
                        if (!historyProductIds.contains(similarId)) {
                            Product product = productService.findById(similarId);
                            if (product != null) {
                                recommendedProducts.add(product);
                            }
                        }
                    }
                }
            }
        }
        
        // 如果推荐数量不足，补充热门产品
        if (recommendedProducts.size() < limit) {
            List<Product> allProducts = productService.findAll();
            // 简单排序，实际应该基于销量、评分等
            allProducts.sort(Comparator.comparing(Product::getPrice).reversed());
            
            for (Product product : allProducts) {
                if (!historyProductIds.contains(product.getId()) && recommendedProducts.size() < limit) {
                    recommendedProducts.add(product);
                }
            }
        }
        
        // 限制推荐数量
        return recommendedProducts.stream().limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public List<Product> recommendByProductId(Integer productId, int limit) {
        // 获取相似产品
        Set<Integer> similarProductIds = memoryStorage.get(PRODUCT_SIMILARITY_KEY + productId);
        List<Product> recommendedProducts = new ArrayList<>();
        
        if (similarProductIds != null) {
            for (Integer similarId : similarProductIds) {
                Product product = productService.findById(similarId);
                if (product != null) {
                    recommendedProducts.add(product);
                }
            }
        }
        
        // 限制推荐数量
        return recommendedProducts.stream().limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public void recordUserView(Integer userId, Integer productId) {
        // 记录用户浏览行为
        String viewKey = USER_VIEW_KEY + userId;
        memoryStorage.computeIfAbsent(viewKey, k -> new HashSet<>()).add(productId);
        
        // 简单实现产品相似度（基于浏览历史）
        Set<Integer> viewedProductIds = memoryStorage.get(viewKey);
        if (viewedProductIds != null) {
            for (Integer viewedId : viewedProductIds) {
                if (!viewedId.equals(productId)) {
                    memoryStorage.computeIfAbsent(PRODUCT_SIMILARITY_KEY + productId, k -> new HashSet<>()).add(viewedId);
                    memoryStorage.computeIfAbsent(PRODUCT_SIMILARITY_KEY + viewedId, k -> new HashSet<>()).add(productId);
                }
            }
        }
    }
    
    @Override
    public void recordUserPurchase(Integer userId, Integer productId) {
        // 记录用户购买行为
        String purchaseKey = USER_PURCHASE_KEY + userId;
        memoryStorage.computeIfAbsent(purchaseKey, k -> new HashSet<>()).add(productId);
    }
}