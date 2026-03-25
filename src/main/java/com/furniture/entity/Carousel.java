package com.furniture.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Carousel {
    private Integer id;
    private String title;
    private String description;
    private String image;
    private Integer productId;
    private Integer sortOrder;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    // 关联的商品信息
    private String productName;
    private Double productPrice;
    private Double productOriginalPrice;
    private Integer productSales;
    private Double productRating;
}