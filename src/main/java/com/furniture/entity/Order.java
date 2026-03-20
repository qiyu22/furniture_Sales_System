package com.furniture.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Order {
    private String id;
    private Integer userId;
    private BigDecimal totalPrice; // 总金额
    private BigDecimal actualPrice; // 实际支付金额
    private Integer status; // 0:待支付, 1:已支付, 2:已发货, 3:已完成, 4:已取消
    private String paymentMethod; // 支付方式
    private Date paymentTime; // 支付时间
    private String shippingAddress; // 收货地址
    private String shippingName; // 收货人姓名
    private String shippingPhone; // 收货人电话
    private Date shippingTime; // 发货时间
    private Date finishTime; // 完成时间
    private Date createdAt;
    private Date updatedAt;
    private List<OrderItem> orderItems; // 订单详情列表
}