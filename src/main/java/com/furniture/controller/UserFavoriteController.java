package com.furniture.controller;

import com.furniture.service.UserFavoriteService;
import com.furniture.utils.JwtUtils;
import com.furniture.utils.Result;
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
                Object userIdObj = claims.get("userId");
                if (userIdObj instanceof Number) {
                    return ((Number) userIdObj).intValue();
                }
                return (Integer) userIdObj;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    
    @ApiOperation("添加收藏")
    @PostMapping("/add")
    public Result addFavorite(
            @ApiParam("商品ID") @RequestParam Integer productId,
            HttpServletRequest request) {
        try {
            Integer userId = getUserId(request);
            if (userId == null) return Result.error("请先登录");
            userFavoriteService.addFavorite(userId, productId);
            return Result.success("收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("收藏失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("取消收藏")
    @PostMapping("/remove")
    public Result removeFavorite(
            @ApiParam("商品ID") @RequestParam Integer productId,
            HttpServletRequest request) {
        try {
            Integer userId = getUserId(request);
            userFavoriteService.removeFavorite(userId, productId);
            return Result.success("取消收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("取消收藏失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("检查是否已收藏")
    @GetMapping("/check")
    public Result checkFavorite(
            @ApiParam("商品ID") @RequestParam Integer productId,
            HttpServletRequest request) {
        try {
            Integer userId = getUserId(request);
            boolean isFavorite = userFavoriteService.isFavorite(userId, productId);
            Map<String, Boolean> data = new HashMap<>();
            data.put("isFavorite", isFavorite);
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("检查失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("获取用户收藏列表")
    @GetMapping("/list")
    public Result getFavoriteList(HttpServletRequest request) {
        try {
            Integer userId = getUserId(request);
            List<Integer> productIds = userFavoriteService.getFavoriteProductIds(userId);
            Map<String, List<Integer>> data = new HashMap<>();
            data.put("productIds", productIds);
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取收藏列表失败: " + e.getMessage());
        }
    }
}