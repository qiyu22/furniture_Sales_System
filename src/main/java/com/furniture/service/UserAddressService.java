package com.furniture.service;

import com.furniture.entity.UserAddress;
import java.util.List;

public interface UserAddressService {
    // 根据用户ID查询地址列表
    List<UserAddress> findByUserId(Integer userId);
    
    // 保存地址
    void save(UserAddress address);
    
    // 删除地址
    void delete(Integer id);
    
    // 设置默认地址
    void setDefault(Integer id);
    
    // 根据ID查询地址
    UserAddress findById(Integer id);
}