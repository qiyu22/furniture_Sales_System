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
}

