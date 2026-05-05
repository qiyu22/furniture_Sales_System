package com.furniture.controller;

import com.furniture.entity.User;
import com.furniture.service.UserService;
import com.furniture.utils.JwtUtils;
import com.furniture.utils.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    public Result login(@ApiParam("用户名") @RequestParam String username, @ApiParam("密码") @RequestParam String password) {
        try {
            Map<String, Object> data = userService.login(username, password);
            if (data != null && data.containsKey("token")) {
                return Result.success(data);
            } else {
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@ApiParam("用户信息") @RequestBody User user) {
        try {
            Map<String, Object> registerResult = userService.register(user);
            if (registerResult.get("success").equals(true)) {
                return Result.success("注册成功");
            } else {
                return Result.error(registerResult.get("message").toString());
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    public Result getUserById(@ApiParam("用户ID") @PathVariable int id) {
        User user = userService.findById(id);
        return Result.success(user);
    }
    
    @ApiOperation("更新用户信息")
    @PutMapping("/{id}")
    public Result updateUser(@ApiParam("用户ID") @PathVariable int id, @ApiParam("用户信息") @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return Result.success("更新成功");
    }
    
    @ApiOperation("获取所有用户")
    @GetMapping
    public Result getAllUsers() {
        List<User> users = userService.findAll();
        return Result.success(users);
    }
    
    @ApiOperation("添加用户")
    @PostMapping
    public Result addUser(@ApiParam("用户信息") @RequestBody User user) {
        Map<String, Object> result = userService.register(user);
        if (result.get("success").equals(true)) {
            return Result.success("添加成功");
        } else {
            return Result.error(result.get("message").toString());
        }
    }
    
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result deleteUser(@ApiParam("用户ID") @PathVariable int id) {
        userService.delete(id);
        return Result.success("删除成功");
    }
    
    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result getCurrentUser(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 解析token获取用户ID
            Claims claims = jwtUtils.parseToken(token);
            Integer userId = (Integer) claims.get("userId");
            if (userId != null) {
                User user = userService.findById(userId);
                return Result.success(user);
            }
        }
        return Result.error("未找到用户信息");
    }
    
    @ApiOperation("更新当前用户信息")
    @PutMapping("/info")
    public Result updateCurrentUser(@ApiParam("用户信息") @RequestBody User user, HttpServletRequest request) {
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
                return Result.success("更新成功");
            }
        }
        return Result.error("未找到用户信息");
    }
    
    @ApiOperation("上传用户头像")
    @PostMapping("/upload-avatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
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
            Map<String, Object> result = new HashMap<>();
            result.put("imageUrl", avatarUrl);
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("修改密码")
    @PostMapping("/change-password")
    public Result changePassword(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
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
                return Result.success("密码修改成功");
            }
        }
        return Result.error("未找到用户信息");
    }
}