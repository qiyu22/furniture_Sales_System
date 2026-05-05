package com.furniture.controller;

import com.furniture.entity.Address;
import com.furniture.service.AddressService;
import com.furniture.utils.JwtUtils;
import com.furniture.utils.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Api(tags = "地址管理")
@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    private Integer getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.parseToken(token);
            return (Integer) claims.get("userId");
        }
        return null;
    }
    
    @ApiOperation("获取用户地址列表")
    @GetMapping
    public Result getUserAddresses(HttpServletRequest request) {
        Integer userId = getUserId(request);
        if (userId == null) {
            return Result.success(Collections.emptyList());
        }
        List<Address> addresses = addressService.findByUserId(userId);
        return Result.success(addresses);
    }
    
    @ApiOperation("添加地址")
    @PostMapping
    public Result addAddress(@ApiParam("地址信息") @RequestBody Address address, HttpServletRequest request) {
        Integer userId = getUserId(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        address.setUserId(userId);
        addressService.add(address);
        return Result.success("添加成功");
    }
    
    @ApiOperation("更新地址")
    @PutMapping("/{id}")
    public Result updateAddress(@ApiParam("地址ID") @PathVariable Integer id, @ApiParam("地址信息") @RequestBody Address address, HttpServletRequest request) {
        Integer userId = getUserId(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        address.setId(id);
        address.setUserId(userId);
        addressService.update(address);
        return Result.success("更新成功");
    }
    
    @ApiOperation("删除地址")
    @DeleteMapping("/{id}")
    public Result deleteAddress(@ApiParam("地址ID") @PathVariable Integer id, HttpServletRequest request) {
        Integer userId = getUserId(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        addressService.delete(id);
        return Result.success("删除成功");
    }
    
    @ApiOperation("设置默认地址")
    @PutMapping("/{id}/default")
    public Result setDefaultAddress(@ApiParam("地址ID") @PathVariable Integer id, HttpServletRequest request) {
        Integer userId = getUserId(request);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        addressService.setDefault(id, userId);
        return Result.success("设置成功");
    }
}