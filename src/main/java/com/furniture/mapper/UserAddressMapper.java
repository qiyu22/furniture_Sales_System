package com.furniture.mapper;

import com.furniture.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAddressMapper {
    // 根据用户ID查询地址列表
    List<UserAddress> findByUserId(@Param("userId") Integer userId);
    
    // 根据ID查询地址
    UserAddress findById(@Param("id") Integer id);
    
    // 插入地址
    void insert(UserAddress address);
    
    // 更新地址
    void update(UserAddress address);
    
    // 删除地址
    void delete(@Param("id") Integer id);
    
    // 重置用户的所有地址为非默认
    void resetDefault(@Param("userId") Integer userId);
    
    // 设置地址为默认
    void setDefault(@Param("id") Integer id);
}