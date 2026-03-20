package com.furniture.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CartItem {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private Integer selected; // 是否选中
    private Date createdAt;
    private Date updatedAt;
    private Product product; // 关联的产品信息
}