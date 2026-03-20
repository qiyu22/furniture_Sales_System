package com.furniture.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Brand {
    private Integer id;
    private String name;
    private String logo;
    private String description;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}