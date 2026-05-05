package com.furniture.controller;

import com.furniture.entity.CustomerServiceMessage;
import com.furniture.service.CustomerServiceMessageService;
import com.furniture.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "客服消息")
@RestController
@RequestMapping("/api/customer-service")
public class CustomerServiceMessageController {

    @Autowired
    private CustomerServiceMessageService customerServiceMessageService;

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public Result sendMessage(@ApiParam("发送者ID") @RequestParam Integer senderId,
                           @ApiParam("发送者名称") @RequestParam String senderName,
                           @ApiParam("接收者ID") @RequestParam Integer recipientId,
                           @ApiParam("消息内容") @RequestParam String message) {
        customerServiceMessageService.sendMessage(senderId, senderName, recipientId, message);
        return Result.success("发送成功");
    }

    @ApiOperation("获取用户消息")
    @GetMapping("/messages")
    public Result getMessages(@ApiParam("用户ID") @RequestParam(required = false) Integer userId) {
        if (userId == null || userId == 0) {
            return Result.success(new ArrayList<>());
        }
        List<CustomerServiceMessage> messages = customerServiceMessageService.getMessagesByUserId(userId);
        return Result.success(messages);
    }

    @ApiOperation("获取所有消息")
    @GetMapping("/messages/all")
    public Result getAllMessages() {
        List<CustomerServiceMessage> messages = customerServiceMessageService.getAllMessages();
        return Result.success(messages);
    }

    @ApiOperation("标记消息为已读")
    @PutMapping("/messages/{id}/read")
    public Result markAsRead(@ApiParam("消息ID") @PathVariable Integer id) {
        customerServiceMessageService.markAsRead(id);
        return Result.success("标记成功");
    }

    @ApiOperation("标记用户所有消息为已读")
    @PutMapping("/messages/read-all")
    public Result markAllAsRead(@ApiParam("用户ID") @RequestParam Integer userId) {
        customerServiceMessageService.markAllAsReadByUserId(userId);
        return Result.success("标记成功");
    }

    @ApiOperation("标记用户所有消息为已读")
    @PostMapping("/messages/read")
    public Result markMessagesAsRead(@ApiParam("用户ID") @RequestParam Integer userId) {
        customerServiceMessageService.markMessagesAsRead(userId);
        return Result.success("标记成功");
    }

    @ApiOperation("新增消息")
    @PostMapping("/messages")
    public Result addMessage(@RequestBody CustomerServiceMessage message) {
        customerServiceMessageService.addMessage(message);
        return Result.success("添加成功");
    }

    @ApiOperation("获取用户所有消息")
    @GetMapping("/messages/all-by-user")
    public Result getAllMessagesByUser(@ApiParam("用户ID") @RequestParam Integer userId) {
        List<CustomerServiceMessage> messages = customerServiceMessageService.getAllMessagesByUserId(userId);
        return Result.success(messages);
    }
}