package com.furniture.entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserBehavior {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String behaviorType;
    private String style;
    private Date createdAt;
}