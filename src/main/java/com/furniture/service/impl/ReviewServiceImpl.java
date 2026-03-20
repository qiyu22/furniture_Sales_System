package com.furniture.service.impl;

import com.furniture.entity.Review;
import com.furniture.mapper.ReviewMapper;
import com.furniture.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewMapper reviewMapper;
    
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
    }
    
    @Override
    public void update(Review review) {
        review.setUpdatedAt(new Date());
        reviewMapper.update(review);
    }
    
    @Override
    public void delete(Integer id) {
        reviewMapper.delete(id);
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