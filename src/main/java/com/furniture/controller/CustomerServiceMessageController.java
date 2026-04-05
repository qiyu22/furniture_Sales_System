package com.furniture.controller;

import com.furniture.entity.CustomerServiceMessage;
import com.furniture.service.CustomerServiceMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "客服消息")
@RestController
@RequestMapping("/api/customer-service")
public class CustomerServiceMessageController {

    @Autowired
    private CustomerServiceMessageService customerServiceMessageService;

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public void sendMessage(@ApiParam("用户ID") @RequestParam Integer userId,
                           @ApiParam("用户名") @RequestParam String userName,
                           @ApiParam("消息内容") @RequestParam String message) {
        customerServiceMessageService.sendMessage(userId, userName, message);
    }

    @ApiOperation("获取用户消息")
    @GetMapping("/messages")
    public List<CustomerServiceMessage> getMessages(@ApiParam("用户ID") @RequestParam(required = false) Integer userId) {
        if (userId == null || userId == 0) {
            return new ArrayList<>();
        }
        return customerServiceMessageService.getMessagesByUserId(userId);
    }

    @ApiOperation("获取所有消息")
    @GetMapping("/messages/all")
    public List<CustomerServiceMessage> getAllMessages() {
        return customerServiceMessageService.getAllMessages();
    }

    @ApiOperation("标记消息为已读")
    @PutMapping("/messages/{id}/read")
    public void markAsRead(@ApiParam("消息ID") @PathVariable Integer id) {
        customerServiceMessageService.markAsRead(id);
    }

    @ApiOperation("标记用户所有消息为已读")
    @PutMapping("/messages/read-all")
    public void markAllAsRead(@ApiParam("用户ID") @RequestParam Integer userId) {
        customerServiceMessageService.markAllAsReadByUserId(userId);
    }

    @ApiOperation("标记用户所有消息为已读")
    @PostMapping("/messages/read")
    public void markMessagesAsRead(@ApiParam("用户ID") @RequestParam Integer userId) {
        customerServiceMessageService.markMessagesAsRead(userId);
    }

    @ApiOperation("新增消息")
    @PostMapping("/messages")
    public void addMessage(@RequestBody CustomerServiceMessage message) {
        customerServiceMessageService.addMessage(message);
    }

    @ApiOperation("获取用户所有消息")
    @GetMapping("/messages/all-by-user")
    public List<CustomerServiceMessage> getAllMessagesByUser(@ApiParam("用户ID") @RequestParam Integer userId) {
        return customerServiceMessageService.getAllMessagesByUserId(userId);
    }
}