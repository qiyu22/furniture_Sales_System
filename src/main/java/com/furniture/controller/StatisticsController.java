package com.furniture.controller;

import com.furniture.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "数据统计")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @ApiOperation("获取销售统计数据")
    @GetMapping("/sales")
    public Map<String, Object> getSalesStatistics(@ApiParam("开始日期") @RequestParam(required = false) String startDate, 
                                               @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        return statisticsService.getSalesStatistics(startDate, endDate);
    }
    
    @ApiOperation("获取产品统计数据")
    @GetMapping("/products")
    public Map<String, Object> getProductStatistics() {
        return statisticsService.getProductStatistics();
    }
    
    @ApiOperation("获取用户统计数据")
    @GetMapping("/users")
    public Map<String, Object> getUserStatistics() {
        return statisticsService.getUserStatistics();
    }
    
    @ApiOperation("获取分类统计数据")
    @GetMapping("/categories")
    public Map<String, Object> getCategoryStatistics() {
        return statisticsService.getCategoryStatistics();
    }
    
    @ApiOperation("获取订单状态统计数据")
    @GetMapping("/order-status")
    public Map<String, Object> getOrderStatusStatistics() {
        return statisticsService.getOrderStatusStatistics();
    }
    
    @ApiOperation("导出销售统计Excel")
    @GetMapping("/export/sales")
    public void exportSalesStatistics(HttpServletResponse response, 
                                     @ApiParam("开始日期") @RequestParam(required = false) String startDate, 
                                     @ApiParam("结束日期") @RequestParam(required = false) String endDate) throws IOException {
        statisticsService.exportSalesStatistics(response, startDate, endDate);
    }
    
    @ApiOperation("导出产品统计Excel")
    @GetMapping("/export/products")
    public void exportProductStatistics(HttpServletResponse response) throws IOException {
        statisticsService.exportProductStatistics(response);
    }
    
    @ApiOperation("导出订单统计Excel")
    @GetMapping("/export/orders")
    public void exportOrderStatistics(HttpServletResponse response, 
                                     @ApiParam("订单状态") @RequestParam(required = false) Integer status, 
                                     @ApiParam("开始日期") @RequestParam(required = false) String startDate, 
                                     @ApiParam("结束日期") @RequestParam(required = false) String endDate) throws IOException {
        statisticsService.exportOrderStatistics(response, status, startDate, endDate);
    }
    
    @ApiOperation("获取仪表盘数据")
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStatistics() {
        return statisticsService.getDashboardStatistics();
    }
    
    @ApiOperation("获取产品销量排行")
    @GetMapping("/product-sales")
    public java.util.List<java.util.Map<String, Object>> getProductSalesRanking() {
        return statisticsService.getProductSalesRanking();
    }
    
    @ApiOperation("获取销量最高的分类")
    @GetMapping("/top-categories")
    public List<Map<String, Object>> getTopCategoriesBySales(@ApiParam("返回数量") @RequestParam(defaultValue = "6") int limit) {
        return statisticsService.getTopCategoriesBySales(limit);
    }
}