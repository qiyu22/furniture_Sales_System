package com.furniture.controller;

import com.furniture.entity.Activity;
import com.furniture.entity.ActivityProduct;
import com.furniture.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "活动管理")
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @ApiOperation("获取所有活动")
    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.findAll();
    }

    @ApiOperation("根据ID获取活动")
    @GetMapping("/{id}")
    public Activity getActivityById(@ApiParam("活动ID") @PathVariable Integer id) {
        return activityService.findById(id);
    }

    @ApiOperation("根据类型获取活动")
    @GetMapping("/type/{type}")
    public List<Activity> getActivitiesByType(@ApiParam("活动类型") @PathVariable String type) {
        return activityService.findByType(type);
    }

    @ApiOperation("获取当前有效的活动")
    @GetMapping("/active")
    public List<Activity> getActiveActivities() {
        return activityService.findActive();
    }

    @ApiOperation("添加活动")
    @PostMapping
    public void addActivity(@ApiParam("活动信息") @RequestBody Activity activity) {
        activityService.add(activity);
    }

    @ApiOperation("更新活动")
    @PutMapping("/{id}")
    public void updateActivity(@ApiParam("活动ID") @PathVariable Integer id, @ApiParam("活动信息") @RequestBody Activity activity) {
        activity.setId(id);
        activityService.update(activity);
    }

    @ApiOperation("删除活动")
    @DeleteMapping("/{id}")
    public void deleteActivity(@ApiParam("活动ID") @PathVariable Integer id) {
        activityService.delete(id);
    }

    @ApiOperation("获取活动商品")
    @GetMapping("/{id}/products")
    public List<ActivityProduct> getActivityProducts(@ApiParam("活动ID") @PathVariable Integer id) {
        return activityService.findProductsByActivityId(id);
    }

    @ApiOperation("添加活动商品")
    @PostMapping("/{id}/products")
    public void addActivityProduct(@ApiParam("活动ID") @PathVariable Integer id, @ApiParam("活动商品信息") @RequestBody ActivityProduct activityProduct) {
        activityProduct.setActivityId(id);
        activityService.addActivityProduct(activityProduct);
    }

    @ApiOperation("删除活动商品")
    @DeleteMapping("/{id}/products")
    public void deleteActivityProduct(@ApiParam("活动ID") @PathVariable Integer id) {
        activityService.deleteActivityProduct(id);
    }

    @ApiOperation("根据活动类型获取活动商品")
    @GetMapping("/type/{type}/products")
    public List<ActivityProduct> getActivityProductsByType(@ApiParam("活动类型") @PathVariable String type) {
        return activityService.findProductsByActivityType(type);
    }
}