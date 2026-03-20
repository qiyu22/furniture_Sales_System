package com.furniture.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {
    private Integer id;
    private String name;
    private Integer categoryId;
    private Integer brandId; // 品牌ID
    private BigDecimal price; // 价格
    private BigDecimal originalPrice; // 原价
    private Integer stock; // 库存
    private Integer sales; // 销量
    private BigDecimal rating; // 评分
    private String description; // 描述
    private String specs; // 规格（JSON格式）
    private String size; // 产品尺寸
    private String image; // 主图
    private String images; // 轮播图（JSON格式）
    private Integer status; // 状态：1-上架，0-下架
    private Date createdAt;
    private Date updatedAt;
}