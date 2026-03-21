package com.furniture.controller;

import com.furniture.service.UserFavoriteService;
import com.furniture.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户收藏管理")
@RestController
@RequestMapping("/api/favorites")
public class UserFavoriteController {
    
    @Autowired
    private UserFavoriteService userFavoriteService;
    
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
    
    @ApiOperation("添加收藏")
    @PostMapping("/add")
    public Map<String, Object> addFavorite(
            @ApiParam("商品ID") @RequestParam Integer productId,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer userId = getUserId(request);
            userFavoriteService.addFavorite(userId, productId);
            result.put("success", true);
            result.put("message", "收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "收藏失败: " + e.getMessage());
        }
        return result;
    }
    
    @ApiOperation("取消收藏")
    @PostMapping("/remove")
    public Map<String, Object> removeFavorite(
            @ApiParam("商品ID") @RequestParam Integer productId,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer userId = getUserId(request);
            userFavoriteService.removeFavorite(userId, productId);
            result.put("success", true);
            result.put("message", "取消收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "取消收藏失败: " + e.getMessage());
        }
        return result;
    }
    
    @ApiOperation("检查是否已收藏")
    @GetMapping("/check")
    public Map<String, Object> checkFavorite(
            @ApiParam("商品ID") @RequestParam Integer productId,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer userId = getUserId(request);
            boolean isFavorite = userFavoriteService.isFavorite(userId, productId);
            result.put("success", true);
            result.put("isFavorite", isFavorite);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "检查失败: " + e.getMessage());
        }
        return result;
    }
    
    @ApiOperation("获取用户收藏列表")
    @GetMapping("/list")
    public Map<String, Object> getFavoriteList(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer userId = getUserId(request);
            List<Integer> productIds = userFavoriteService.getFavoriteProductIds(userId);
            result.put("success", true);
            result.put("productIds", productIds);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取收藏列表失败: " + e.getMessage());
        }
        return result;
    }
}