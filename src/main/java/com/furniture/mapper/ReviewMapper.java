package com.furniture.mapper;

import com.furniture.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {
    // 根据用户ID查询评价列表
    List<Review> findByUserId(@Param("userId") Integer userId);
    
    // 查询所有评价
    List<Review> findAll();
    
    // 根据ID查询评价
    Review findById(@Param("id") Integer id);
    
    // 插入评价
    void insert(Review review);
    
    // 更新评价
    void update(Review review);
    
    // 删除评价
    void delete(@Param("id") Integer id);
    
    // 根据商品ID查询评价
    List<Review> findByProductId(@Param("productId") Integer productId);
    
    // 查询所有评价，包含商品名称
    List<Map<String, Object>> findAllWithProductName();
    
    // 根据ID查询评价，包含商品名称
    Map<String, Object> findByIdWithProductName(@Param("id") Integer id);
    
    // 根据商品ID查询评价，包含商品名称
    List<Map<String, Object>> findByProductIdWithProductName(@Param("productId") Integer productId);
    
    // 查询最新评价，包含商品名称
    List<Map<String, Object>> findLatestWithProductName();
}