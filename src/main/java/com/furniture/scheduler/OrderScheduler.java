package com.furniture.scheduler;

import com.furniture.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 订单定时任务
 */
@Component
public class OrderScheduler {

    @Autowired
    private OrderService orderService;

    /**
     * 每小时执行一次，自动取消超时未付款的订单
     */
    @Scheduled(cron = "0 0 * * * ?") // 每小时整点执行
    public void autoCancelTimeoutOrders() {
        System.out.println("开始执行自动取消超时未付款订单任务：" + new Date());
        try {
            // 取消超过2小时未付款的订单
            int cancelledCount = orderService.autoCancelTimeoutOrders(2);
            System.out.println("自动取消超时未付款订单完成，共取消" + cancelledCount + "个订单：" + new Date());
        } catch (Exception e) {
            System.err.println("自动取消超时未付款订单失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}