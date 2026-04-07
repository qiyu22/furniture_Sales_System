package com.furniture.service.impl;

import com.furniture.entity.CustomerServiceMessage;
import com.furniture.mapper.CustomerServiceMessageMapper;
import com.furniture.service.CustomerServiceMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceMessageServiceImpl implements CustomerServiceMessageService {

    @Autowired
    private CustomerServiceMessageMapper customerServiceMessageMapper;

    @Override
    public void sendMessage(Integer senderId, String senderName, Integer recipientId, String message) {
        CustomerServiceMessage customerServiceMessage = new CustomerServiceMessage();
        customerServiceMessage.setSenderId(senderId);
        customerServiceMessage.setSenderName(senderName);
        customerServiceMessage.setRecipientId(recipientId);
        customerServiceMessage.setMessage(message);
        customerServiceMessage.setStatus(0);
        customerServiceMessage.setCreatedAt(LocalDateTime.now());
        customerServiceMessageMapper.insert(customerServiceMessage);
    }

    @Override
    public List<CustomerServiceMessage> getMessagesByUserId(Integer userId) {
        return customerServiceMessageMapper.findByUserId(userId);
    }

    @Override
    public List<CustomerServiceMessage> getAllMessages() {
        return customerServiceMessageMapper.findAll();
    }

    @Override
    public List<CustomerServiceMessage> getAllMessagesByUserId(Integer userId) {
        return customerServiceMessageMapper.findAllByUserId(userId);
    }

    @Override
    public void markAsRead(Integer id) {
        customerServiceMessageMapper.updateStatus(id, 1);
    }

    @Override
    public void markAllAsReadByUserId(Integer userId) {
        customerServiceMessageMapper.updateStatusByUserId(userId, 1);
    }

    @Override
    public void markMessagesAsRead(Integer userId) {
        customerServiceMessageMapper.updateStatusByUserId(userId, 1);
    }

    @Override
    public void addMessage(CustomerServiceMessage message) {
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(LocalDateTime.now());
        }
        if (message.getStatus() == null) {
            message.setStatus(0); // 默认未读
        }
        customerServiceMessageMapper.insert(message);
    }
}