package com.furniture.service.impl;

import com.furniture.entity.Review;
import com.furniture.entity.Product;
import com.furniture.mapper.ReviewMapper;
import com.furniture.service.ReviewService;
import com.furniture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewMapper reviewMapper;
    
    @Autowired
    private ProductService productService;
    
    @Override
    public List<Review> findByUserId(Integer userId) {
        return reviewMapper.findByUserId(userId);
    }
    
    @Override
    public Review findById(Integer id) {
        return reviewMapper.findById(id);
    }
    
    @Override
    public void save(Review review) {
        if (review.getId() == null) {
            // 新增
            review.setCreatedAt(new Date());
            review.setUpdatedAt(new Date());
            reviewMapper.insert(review);
        } else {
            // 更新
            review.setUpdatedAt(new Date());
            reviewMapper.update(review);
        }
        
        // 更新商品评分
        updateProductRating(review.getProductId());
    }
    
    @Override
    public void update(Review review) {
        review.setUpdatedAt(new Date());
        reviewMapper.update(review);
        
        // 更新商品评分
        updateProductRating(review.getProductId());
    }
    
    @Override
    public void delete(Integer id) {
        // 获取评价信息，用于更新商品评分
        Review review = reviewMapper.findById(id);
        if (review != null) {
            reviewMapper.delete(id);
            // 更新商品评分
            updateProductRating(review.getProductId());
        } else {
            reviewMapper.delete(id);
        }
    }
    
    /**
     * 更新商品评分
     * @param productId 商品ID
     */
    private void updateProductRating(Integer productId) {
        if (productId == null) {
            return;
        }
        
        // 获取该商品的所有评价
        List<Review> reviews = reviewMapper.findByProductId(productId);
        if (reviews == null || reviews.isEmpty()) {
            // 没有评价，设置评分为0
            Product product = productService.findById(productId);
            if (product != null) {
                product.setRating(BigDecimal.ZERO);
                productService.update(product);
            }
            return;
        }
        
        // 计算平均评分
        BigDecimal totalRating = BigDecimal.ZERO;
        for (Review review : reviews) {
            if (review.getRating() != null) {
                // 将 Integer 转换为 BigDecimal
                totalRating = totalRating.add(BigDecimal.valueOf(review.getRating()));
            }
        }
        
        BigDecimal averageRating = totalRating.divide(BigDecimal.valueOf(reviews.size()), 1, BigDecimal.ROUND_HALF_UP);
        
        // 更新商品评分
        Product product = productService.findById(productId);
        if (product != null) {
            product.setRating(averageRating);
            productService.update(product);
        }
    }
    
    @Override
    public List<Review> findAll() {
        return reviewMapper.findAll();
    }
    
    @Override
    public List<Review> findByProductId(Integer productId) {
        return reviewMapper.findByProductId(productId);
    }
    
    @Override
    public List<Map<String, Object>> findAllWithProductName() {
        return reviewMapper.findAllWithProductName();
    }
    
    @Override
    public Map<String, Object> findByIdWithProductName(Integer id) {
        return reviewMapper.findByIdWithProductName(id);
    }
    
    @Override
    public List<Map<String, Object>> findByProductIdWithProductName(Integer productId) {
        return reviewMapper.findByProductIdWithProductName(productId);
    }
    
    @Override
    public List<Map<String, Object>> findLatestWithProductName() {
        return reviewMapper.findLatestWithProductName();
    }
}