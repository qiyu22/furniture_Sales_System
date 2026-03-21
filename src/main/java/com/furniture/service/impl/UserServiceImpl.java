package com.furniture.service.impl;

import com.furniture.entity.User;
import com.furniture.mapper.UserMapper;
import com.furniture.service.UserService;

import com.furniture.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userMapper.findByUsername(username);
        Map<String, Object> result = new HashMap<>();
        
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            // 生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());
            claims.put("userId", user.getId());
            String token = jwtUtils.generateToken(username, claims);
            
            result.put("token", token);
            result.put("user", user);
            return result;
        }
        return null;
    }
    
    @Override
    public void register(User user) {
        // 密码加密
        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(encryptedPassword);
        user.setRole("USER"); // 默认角色为普通用户
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        userMapper.insert(user);
    }
    
    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }
    
    @Override
    public void update(User user) {
        // 先获取原始用户信息
        User originalUser = userMapper.findById(user.getId());
        if (originalUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 如果密码不为空，则对密码进行加密处理
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // 检查密码是否已经加密（BCrypt加密的密码长度较长，通常以$2a$开头）
            if (!user.getPassword().startsWith("$2a$")) {
                String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                user.setPassword(encryptedPassword);
            }
        } else {
            // 如果密码为空，则使用原始密码
            user.setPassword(originalUser.getPassword());
        }
        
        // 如果姓名为空，则使用原始姓名
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(originalUser.getName());
        }
        
        // 如果地址为空，则使用原始地址
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            user.setAddress(originalUser.getAddress());
        }
        
        // 如果角色为空，则使用原始角色
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole(originalUser.getRole());
        }
        
        // 如果状态为空，则使用原始状态
        if (user.getStatus() == null) {
            user.setStatus(originalUser.getStatus());
        }
        
        // 设置更新时间
        user.setUpdatedAt(new Date());
        
        // 更新用户信息
        userMapper.update(user);
    }
    
    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
    
    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }
    
    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        // 根据用户ID获取用户信息
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证旧密码是否正确
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 加密新密码
        String encryptedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(encryptedPassword);
        user.setUpdatedAt(new Date());
        
        // 更新用户信息
        userMapper.update(user);
    }
}