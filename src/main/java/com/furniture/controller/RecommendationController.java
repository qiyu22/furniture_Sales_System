package com.furniture.controller;

import com.furniture.entity.Product;
import com.furniture.service.RecommendationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "智能推荐")
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @ApiOperation("根据用户ID推荐产品")
    @GetMapping("/user/{userId}")
    public List<Product> recommendByUserId(@ApiParam("用户ID") @PathVariable int userId, 
                                         @ApiParam("推荐数量") @RequestParam(defaultValue = "10") int limit) {
        return recommendationService.recommendByUserId(userId, limit);
    }
    
    @ApiOperation("根据产品ID推荐相关产品")
    @GetMapping("/product/{productId}")
    public List<Product> recommendByProductId(@ApiParam("产品ID") @PathVariable int productId, 
                                            @ApiParam("推荐数量") @RequestParam(defaultValue = "10") int limit) {
        return recommendationService.recommendByProductId(productId, limit);
    }
    
    @ApiOperation("记录用户浏览行为")
    @PostMapping("/view")
    public void recordUserView(@ApiParam("用户ID") @RequestParam int userId, 
                             @ApiParam("产品ID") @RequestParam int productId) {
        recommendationService.recordUserView(userId, productId);
    }
    
    @ApiOperation("记录用户购买行为")
    @PostMapping("/purchase")
    public void recordUserPurchase(@ApiParam("用户ID") @RequestParam int userId, 
                                 @ApiParam("产品ID") @RequestParam int productId) {
        recommendationService.recordUserPurchase(userId, productId);
    }
}