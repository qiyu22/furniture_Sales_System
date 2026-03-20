package com.furniture.service;

import com.furniture.entity.Address;
import java.util.List;

public interface AddressService {
    // 根据用户ID查询地址列表
    List<Address> findByUserId(Integer userId);
    
    // 根据ID查询地址
    Address findById(Integer id);
    
    // 添加地址
    void add(Address address);
    
    // 更新地址
    void update(Address address);
    
    // 删除地址
    void delete(Integer id);
    
    // 设置默认地址
    void setDefault(Integer id, Integer userId);
}