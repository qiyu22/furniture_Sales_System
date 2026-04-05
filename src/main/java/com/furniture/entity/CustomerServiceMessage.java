package com.furniture.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerServiceMessage {
    private Integer id;
    private Integer userId; // 发送者ID，0表示客服
    private String userName; // 发送者名称
    private String message; // 消息内容
    private Integer status; // 0:未读, 1:已读
    private LocalDateTime createdAt; // 创建时间
}