package com.furniture.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String role; // 角色：USER 或 ADMIN
    private Integer status; // 状态：1-正常，0-禁用
    private String avatar; // 头像URL
    private Date createdAt;
    private Date updatedAt;
}