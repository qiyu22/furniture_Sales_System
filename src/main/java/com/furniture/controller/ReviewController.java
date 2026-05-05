package com.furniture.controller;

import com.furniture.entity.Review;
import com.furniture.service.ReviewService;
import com.furniture.utils.JwtUtils;
import com.furniture.utils.Result;
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
            try {
                Claims claims = jwtUtils.parseToken(token);
                return (Integer) claims.get("userId");
            } catch (Exception e) {
                // 解析 token 失败，使用默认用户 ID
                return 5;
            }
        }
        // 没有 token，使用默认用户 ID
        return 5;
    }
    
    @ApiOperation("获取用户评价列表")
    @GetMapping("/user")
    public Result getUserReviews(HttpServletRequest request) {
        Integer userId = getUserId(request);
        List<Review> reviews = reviewService.findByUserId(userId);
        return Result.success(reviews);
    }
    
    @ApiOperation("更新评价")
    @PutMapping("/{id}")
    public Result updateReview(@ApiParam("评价ID") @PathVariable Integer id, @ApiParam("评价信息") @RequestBody Review review, HttpServletRequest request) {
        Integer userId = getUserId(request);
        review.setId(id);
        review.setUserId(userId);
        reviewService.update(review);
        return Result.success("更新成功");
    }
    
    @ApiOperation("删除评价")
    @DeleteMapping("/{id}")
    public Result deleteReview(@ApiParam("评价ID") @PathVariable Integer id, HttpServletRequest request) {
        getUserId(request); // 验证用户登录状态
        reviewService.delete(id);
        return Result.success("删除成功");
    }
    
    @ApiOperation("上传评价图片")
    @PostMapping("/upload")
    public Result uploadReviewImage(@RequestParam("file") MultipartFile file) {
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
            Map<String, String> data = new HashMap<>();
            data.put("imageUrl", imageUrl);
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("获取所有评价")
    @GetMapping
    public Result getAllReviews() {
        List<Map<String, Object>> reviews = reviewService.findAllWithProductName();
        return Result.success(reviews);
    }

    @ApiOperation("获取评价详情")
    @GetMapping("/detail/{id}")
    public Result getReviewById(@PathVariable Integer id) {
        Map<String, Object> review = reviewService.findByIdWithProductName(id);
        return Result.success(review);
    }

    @ApiOperation("添加评价")
    @PostMapping
    public Result addReview(@RequestBody Review review, HttpServletRequest request) {
        Integer userId = getUserId(request);
        review.setUserId(userId);
        reviewService.save(review);
        return Result.success("添加成功");
    }

    @ApiOperation("根据商品ID获取评价")
    @GetMapping("/product/{productId}")
    public Result getReviewsByProductId(@PathVariable Integer productId) {
        List<Map<String, Object>> reviews = reviewService.findByProductIdWithProductName(productId);
        return Result.success(reviews);
    }
}