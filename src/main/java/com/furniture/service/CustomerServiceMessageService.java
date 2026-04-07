package com.furniture.service;

import com.furniture.entity.CustomerServiceMessage;
import java.util.List;

public interface CustomerServiceMessageService {
    void sendMessage(Integer senderId, String senderName, Integer recipientId, String message);
    List<CustomerServiceMessage> getMessagesByUserId(Integer userId);
    List<CustomerServiceMessage> getAllMessagesByUserId(Integer userId);
    List<CustomerServiceMessage> getAllMessages();
    void markAsRead(Integer id);
    void markAllAsReadByUserId(Integer userId);
    void markMessagesAsRead(Integer userId);
    void addMessage(CustomerServiceMessage message);
}