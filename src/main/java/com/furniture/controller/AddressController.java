package com.furniture.controller;

import com.furniture.entity.Address;
import com.furniture.service.AddressService;
import com.furniture.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        throw new RuntimeException("未找到用户信息");
    }
    
    @ApiOperation("获取用户地址列表")
    @GetMapping
    public List<Address> getUserAddresses(HttpServletRequest request) {
        Integer userId = getUserId(request);
        return addressService.findByUserId(userId);
    }
    
    @ApiOperation("添加地址")
    @PostMapping
    public void addAddress(@ApiParam("地址信息") @RequestBody Address address, HttpServletRequest request) {
        Integer userId = getUserId(request);
        address.setUserId(userId);
        addressService.add(address);
    }
    
    @ApiOperation("更新地址")
    @PutMapping("/{id}")
    public void updateAddress(@ApiParam("地址ID") @PathVariable Integer id, @ApiParam("地址信息") @RequestBody Address address, HttpServletRequest request) {
        Integer userId = getUserId(request);
        address.setId(id);
        address.setUserId(userId);
        addressService.update(address);
    }
    
    @ApiOperation("删除地址")
    @DeleteMapping("/{id}")
    public void deleteAddress(@ApiParam("地址ID") @PathVariable Integer id, HttpServletRequest request) {
        getUserId(request); // 验证用户登录状态
        addressService.delete(id);
    }
    
    @ApiOperation("设置默认地址")
    @PutMapping("/{id}/default")
    public void setDefaultAddress(@ApiParam("地址ID") @PathVariable Integer id, HttpServletRequest request) {
        Integer userId = getUserId(request);
        addressService.setDefault(id, userId);
    }
}