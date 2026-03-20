package com.furniture.service;

import com.furniture.entity.User;
import java.util.List;
import java.util.Map;

public interface UserService {
    // 用户登录
    Map<String, Object> login(String username, String password);
    
    // 用户注册
    void register(User user);
    
    // 根据ID查询用户
    User findById(Integer id);
    
    // 更新用户信息
    void update(User user);
    
    // 获取所有用户
    List<User> findAll();
    
    // 删除用户
    void delete(Integer id);
    
    // 修改密码
    void changePassword(Integer userId, String oldPassword, String newPassword);
}