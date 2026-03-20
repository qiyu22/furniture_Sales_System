package com.furniture.controller;

import com.furniture.entity.Review;
import com.furniture.service.ReviewService;
import com.furniture.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "评价管理")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    private Integer getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.parseToken(token);
            return (Integer) claims.get("userId");
        }
        throw new RuntimeException("未找到用户信息");
    }
    
    @ApiOperation("获取用户评价列表")
    @GetMapping("/user")
    public List<Review> getUserReviews(HttpServletRequest request) {
        Integer userId = getUserId(request);
        return reviewService.findByUserId(userId);
    }
    
    @ApiOperation("更新评价")
    @PutMapping("/{id}")
    public void updateReview(@ApiParam("评价ID") @PathVariable Integer id, @ApiParam("评价信息") @RequestBody Review review, HttpServletRequest request) {
        Integer userId = getUserId(request);
        review.setId(id);
        review.setUserId(userId);
        reviewService.update(review);
    }
    
    @ApiOperation("删除评价")
    @DeleteMapping("/{id}")
    public void deleteReview(@ApiParam("评价ID") @PathVariable Integer id, HttpServletRequest request) {
        getUserId(request); // 验证用户登录状态
        reviewService.delete(id);
    }
    
    @ApiOperation("上传评价图片")
    @PostMapping("/upload")
    public Map<String, Object> uploadReviewImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 确保上传目录存在
            String uploadDir = "D:\\Code_items\\furniture_Sales_System\\frontend\\public\\review-images";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 生成唯一文件名
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File dest = new File(uploadDir + File.separator + fileName);
            
            // 保存文件
            file.transferTo(dest);
            
            // 构建本地访问URL
            String imageUrl = "/review-images/" + fileName;
            result.put("imageUrl", imageUrl);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "上传失败: " + e.getMessage());
        }
        return result;
    }
    
    @ApiOperation("获取所有评价")
    @GetMapping
    public List<Map<String, Object>> getAllReviews() {
        return reviewService.findAllWithProductName();
    }

    @ApiOperation("获取评价详情")
    @GetMapping("/detail/{id}")
    public Map<String, Object> getReviewById(@PathVariable Integer id) {
        return reviewService.findByIdWithProductName(id);
    }

    @ApiOperation("添加评价")
    @PostMapping
    public void addReview(@RequestBody Review review, HttpServletRequest request) {
        Integer userId = getUserId(request);
        review.setUserId(userId);
        reviewService.save(review);
    }

    @ApiOperation("根据商品ID获取评价")
    @GetMapping("/product/{productId}")
    public List<Map<String, Object>> getReviewsByProductId(@PathVariable Integer productId) {
        return reviewService.findByProductIdWithProductName(productId);
    }
}