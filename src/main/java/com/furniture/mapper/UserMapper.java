package com.furniture.mapper;

import com.furniture.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    User findByUsername(@Param("username") String username);
    
    // 根据ID查询用户
    User findById(@Param("id") Integer id);
    
    // 插入用户
    void insert(User user);
    
    // 更新用户信息
    void update(User user);
    
    // 根据ID更新用户状态
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    // 查询所有用户
    List<User> findAll();
    
    // 删除用户
    void delete(@Param("id") Integer id);
}