package com.furniture.controller;

import com.furniture.service.StatisticsService;
import com.furniture.utils.Result;
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
    public Result getSalesStatistics(@ApiParam("开始日期") @RequestParam(required = false) String startDate, 
                                               @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        Map<String, Object> statistics = statisticsService.getSalesStatistics(startDate, endDate);
        return Result.success(statistics);
    }
    
    @ApiOperation("获取产品统计数据")
    @GetMapping("/products")
    public Result getProductStatistics() {
        Map<String, Object> statistics = statisticsService.getProductStatistics();
        return Result.success(statistics);
    }
    
    @ApiOperation("获取用户统计数据")
    @GetMapping("/users")
    public Result getUserStatistics() {
        Map<String, Object> statistics = statisticsService.getUserStatistics();
        return Result.success(statistics);
    }
    
    @ApiOperation("获取分类统计数据")
    @GetMapping("/categories")
    public Result getCategoryStatistics() {
        Map<String, Object> statistics = statisticsService.getCategoryStatistics();
        return Result.success(statistics);
    }
    
    @ApiOperation("获取订单状态统计数据")
    @GetMapping("/order-status")
    public Result getOrderStatusStatistics() {
        Map<String, Object> statistics = statisticsService.getOrderStatusStatistics();
        return Result.success(statistics);
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
    public Result getDashboardStatistics() {
        Map<String, Object> statistics = statisticsService.getDashboardStatistics();
        return Result.success(statistics);
    }
    
    @ApiOperation("获取产品销量排行")
    @GetMapping("/product-sales")
    public Result getProductSalesRanking() {
        List<Map<String, Object>> ranking = statisticsService.getProductSalesRanking();
        return Result.success(ranking);
    }
    
    @ApiOperation("获取销量最高的分类")
    @GetMapping("/top-categories")
    public Result getTopCategoriesBySales(@ApiParam("返回数量") @RequestParam(defaultValue = "6") int limit) {
        List<Map<String, Object>> categories = statisticsService.getTopCategoriesBySales(limit);
        return Result.success(categories);
    }
}