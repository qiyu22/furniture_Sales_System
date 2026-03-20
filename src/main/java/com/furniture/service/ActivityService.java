package com.furniture.service;

import com.furniture.entity.Activity;
import com.furniture.entity.ActivityProduct;

import java.util.List;

public interface ActivityService {
    // 获取所有活动
    List<Activity> findAll();
    
    // 根据ID获取活动
    Activity findById(Integer id);
    
    // 根据类型获取活动
    List<Activity> findByType(String type);
    
    // 获取当前有效的活动
    List<Activity> findActive();
    
    // 添加活动
    void add(Activity activity);
    
    // 更新活动
    void update(Activity activity);
    
    // 删除活动
    void delete(Integer id);
    
    // 获取活动商品
    List<ActivityProduct> findProductsByActivityId(Integer activityId);
    
    // 添加活动商品
    void addActivityProduct(ActivityProduct activityProduct);
    
    // 更新活动商品
    void updateActivityProduct(ActivityProduct activityProduct);
    
    // 删除活动商品
    void deleteActivityProduct(Integer activityId);
    
    // 根据活动类型获取活动商品
    List<ActivityProduct> findProductsByActivityType(String type);
    
    // 根据产品ID获取活动商品
    List<ActivityProduct> findProductsByProductId(Integer productId);
}