package com.furniture.service;

import com.furniture.entity.Review;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    List<Review> findAll();
    Review findById(Integer id);
    void save(Review review);
    void delete(Integer id);
    List<Review> findByProductId(Integer productId);
    List<Map<String, Object>> findAllWithProductName();
    Map<String, Object> findByIdWithProductName(Integer id);
    List<Map<String, Object>> findByProductIdWithProductName(Integer productId);
    List<Map<String, Object>> findLatestWithProductName();
    List<Review> findByUserId(Integer userId);
    void update(Review review);
}