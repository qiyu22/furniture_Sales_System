package com.furniture.mapper;

import com.furniture.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    // 根据用户ID查询地址列表
    List<Address> findByUserId(@Param("userId") Integer userId);
    
    // 根据ID查询地址
    Address findById(@Param("id") Integer id);
    
    // 插入地址
    void insert(Address address);
    
    // 更新地址
    void update(Address address);
    
    // 删除地址
    void delete(@Param("id") Integer id);
    
    // 设置默认地址
    void setDefault(@Param("userId") Integer userId);
    
    // 设置指定地址为默认
    void setDefaultById(@Param("id") Integer id);
}