package com.furniture.entity;

import lombok.Data;
import java.util.Date;

import com.furniture.entity.Product;

@Data
public class Review {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String orderId;
    private Integer rating;
    private String content;
    private String images;
    private Date createdAt;
    private Date updatedAt;
    private Product product;
}