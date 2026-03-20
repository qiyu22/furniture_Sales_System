package com.furniture.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Category {
    private Integer id;
    private String name;
    private Integer parentId; // 父分类ID
    private String description;
    private Integer sortOrder; // 排序权重
    private Integer status; // 状态：1-启用，0-禁用
    private Date createdAt;
    private Date updatedAt;
}