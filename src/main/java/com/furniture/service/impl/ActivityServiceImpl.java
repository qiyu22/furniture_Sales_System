package com.furniture.service.impl;

import com.furniture.entity.Activity;
import com.furniture.entity.ActivityProduct;
import com.furniture.mapper.ActivityMapper;
import com.furniture.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Activity> findAll() {
        return activityMapper.findAll();
    }

    @Override
    public Activity findById(Integer id) {
        return activityMapper.findById(id);
    }

    @Override
    public List<Activity> findByType(String type) {
        return activityMapper.findByType(type);
    }

    @Override
    public List<Activity> findActive() {
        return activityMapper.findActive();
    }

    @Override
    public void add(Activity activity) {
        activity.setCreatedAt(new Date());
        activity.setUpdatedAt(new Date());
        activityMapper.insert(activity);
    }

    @Override
    public void update(Activity activity) {
        activity.setUpdatedAt(new Date());
        activityMapper.update(activity);
    }

    @Override
    public void delete(Integer id) {
        activityMapper.delete(id);
    }

    @Override
    public List<ActivityProduct> findProductsByActivityId(Integer activityId) {
        return activityMapper.findProductsByActivityId(activityId);
    }

    @Override
    public void addActivityProduct(ActivityProduct activityProduct) {
        activityProduct.setCreatedAt(new Date());
        activityMapper.insertActivityProduct(activityProduct);
    }

    @Override
    public void updateActivityProduct(ActivityProduct activityProduct) {
        // 先删除旧的关联
        activityMapper.deleteActivityProduct(activityProduct.getActivityId());
        // 再添加新的关联
        activityProduct.setCreatedAt(new Date());
        activityMapper.insertActivityProduct(activityProduct);
    }

    @Override
    public void deleteActivityProduct(Integer activityId) {
        activityMapper.deleteActivityProduct(activityId);
    }

    @Override
    public List<ActivityProduct> findProductsByActivityType(String type) {
        return activityMapper.findProductsByActivityType(type);
    }
    
    @Override
    public List<ActivityProduct> findProductsByProductId(Integer productId) {
        return activityMapper.findProductsByProductId(productId);
    }
}