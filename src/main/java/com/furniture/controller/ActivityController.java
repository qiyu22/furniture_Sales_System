package com.furniture.controller;

import com.furniture.entity.Activity;
import com.furniture.entity.ActivityProduct;
import com.furniture.service.ActivityService;
import com.furniture.utils.Result;
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
    public Result getAllActivities() {
        List<Activity> activities = activityService.findAll();
        return Result.success(activities);
    }

    @ApiOperation("根据ID获取活动")
    @GetMapping("/{id}")
    public Result getActivityById(@ApiParam("活动ID") @PathVariable Integer id) {
        Activity activity = activityService.findById(id);
        return Result.success(activity);
    }

    @ApiOperation("根据类型获取活动")
    @GetMapping("/type/{type}")
    public Result getActivitiesByType(@ApiParam("活动类型") @PathVariable String type) {
        List<Activity> activities = activityService.findByType(type);
        return Result.success(activities);
    }

    @ApiOperation("获取当前有效的活动")
    @GetMapping("/active")
    public Result getActiveActivities() {
        List<Activity> activities = activityService.findActive();
        return Result.success(activities);
    }

    @ApiOperation("添加活动")
    @PostMapping
    public Result addActivity(@ApiParam("活动信息") @RequestBody Activity activity) {
        activityService.add(activity);
        return Result.success(activity);
    }

    @ApiOperation("更新活动")
    @PutMapping("/{id}")
    public Result updateActivity(@ApiParam("活动ID") @PathVariable Integer id, @ApiParam("活动信息") @RequestBody Activity activity) {
        activity.setId(id);
        activityService.update(activity);
        return Result.success("更新成功");
    }

    @ApiOperation("删除活动")
    @DeleteMapping("/{id}")
    public Result deleteActivity(@ApiParam("活动ID") @PathVariable Integer id) {
        activityService.delete(id);
        return Result.success("删除成功");
    }

    @ApiOperation("获取活动商品")
    @GetMapping("/{id}/products")
    public Result getActivityProducts(@ApiParam("活动ID") @PathVariable Integer id) {
        List<ActivityProduct> products = activityService.findProductsByActivityId(id);
        return Result.success(products);
    }

    @ApiOperation("添加活动商品")
    @PostMapping("/{id}/products")
    public Result addActivityProduct(@ApiParam("活动ID") @PathVariable Integer id, @ApiParam("活动商品信息") @RequestBody ActivityProduct activityProduct) {
        activityProduct.setActivityId(id);
        activityService.addActivityProduct(activityProduct);
        return Result.success("添加成功");
    }

    @ApiOperation("删除活动商品")
    @DeleteMapping("/{id}/products")
    public Result deleteActivityProduct(@ApiParam("活动ID") @PathVariable Integer id) {
        activityService.deleteActivityProduct(id);
        return Result.success("删除成功");
    }

    @ApiOperation("根据活动类型获取活动商品")
    @GetMapping("/type/{type}/products")
    public Result getActivityProductsByType(@ApiParam("活动类型") @PathVariable String type) {
        List<ActivityProduct> products = activityService.findProductsByActivityType(type);
        return Result.success(products);
    }
}