package com.furniture.entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserAddress {
    private Integer id;
    private Integer userId;
    private String name;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
    private Date createdAt;
    private Date updatedAt;
}