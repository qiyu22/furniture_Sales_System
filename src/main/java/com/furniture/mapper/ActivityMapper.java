package com.furniture.mapper;

import com.furniture.entity.Activity;
import com.furniture.entity.ActivityProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper {
    // 查询所有活动
    List<Activity> findAll();
    
    // 根据ID查询活动
    Activity findById(@Param("id") Integer id);
    
    // 根据类型查询活动
    List<Activity> findByType(@Param("type") String type);
    
    // 查询当前有效的活动
    List<Activity> findActive();
    
    // 插入活动
    void insert(Activity activity);
    
    // 更新活动
    void update(Activity activity);
    
    // 删除活动
    void delete(@Param("id") Integer id);
    
    // 查询活动商品
    List<ActivityProduct> findProductsByActivityId(@Param("activityId") Integer activityId);
    
    // 插入活动商品
    void insertActivityProduct(ActivityProduct activityProduct);
    
    // 删除活动商品
    void deleteActivityProduct(@Param("activityId") Integer activityId);
    
    // 根据活动类型查询活动商品
    List<ActivityProduct> findProductsByActivityType(@Param("type") String type);
    
    // 根据产品ID查询活动商品
    List<ActivityProduct> findProductsByProductId(@Param("productId") Integer productId);
}