package com.furniture.controller;

import com.furniture.entity.User;
import com.furniture.service.UserService;
import com.furniture.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Object login(@ApiParam("用户名") @RequestParam String username, @ApiParam("密码") @RequestParam String password) {
        return userService.login(username, password);
    }
    
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public void register(@ApiParam("用户信息") @RequestBody User user) {
        userService.register(user);
    }
    
    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    public User getUserById(@ApiParam("用户ID") @PathVariable int id) {
        return userService.findById(id);
    }
    
    @ApiOperation("更新用户信息")
    @PutMapping("/{id}")
    public void updateUser(@ApiParam("用户ID") @PathVariable int id, @ApiParam("用户信息") @RequestBody User user) {
        user.setId(id);
        userService.update(user);
    }
    
    @ApiOperation("获取所有用户")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @ApiOperation("添加用户")
    @PostMapping
    public void addUser(@ApiParam("用户信息") @RequestBody User user) {
        userService.register(user);
    }
    
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public void deleteUser(@ApiParam("用户ID") @PathVariable int id) {
        userService.delete(id);
    }
    
    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public User getCurrentUser(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 解析token获取用户ID
            Claims claims = jwtUtils.parseToken(token);
            Integer userId = (Integer) claims.get("userId");
            if (userId != null) {
                return userService.findById(userId);
            }
        }
        throw new RuntimeException("未找到用户信息");
    }
    
    @ApiOperation("更新当前用户信息")
    @PutMapping("/info")
    public void updateCurrentUser(@ApiParam("用户信息") @RequestBody User user, HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 解析token获取用户ID
            Claims claims = jwtUtils.parseToken(token);
            Integer userId = (Integer) claims.get("userId");
            if (userId != null) {
                user.setId(userId);
                userService.update(user);
                return;
            }
        }
        throw new RuntimeException("未找到用户信息");
    }
    
    @ApiOperation("上传用户头像")
    @PostMapping("/upload-avatar")
    public Map<String, Object> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 确保上传目录存在
            String uploadDir = "D:\\Code_items\\furniture_Sales_System\\frontend\\public\\avatars";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 生成唯一文件名
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File dest = new File(uploadDir + File.separator + fileName);
            
            // 保存文件
            file.transferTo(dest);
            
            // 构建本地访问URL
            String avatarUrl = "/avatars/" + fileName;
            result.put("imageUrl", avatarUrl);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "上传失败: " + e.getMessage());
        }
        return result;
    }
    
    @ApiOperation("修改密码")
    @PostMapping("/change-password")
    public void changePassword(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        // 从请求头中获取token
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 解析token获取用户ID
            Claims claims = jwtUtils.parseToken(token);
            Integer userId = (Integer) claims.get("userId");
            if (userId != null) {
                String oldPassword = request.get("oldPassword");
                String newPassword = request.get("newPassword");
                userService.changePassword(userId, oldPassword, newPassword);
                return;
            }
        }
        throw new RuntimeException("未找到用户信息");
    }
}