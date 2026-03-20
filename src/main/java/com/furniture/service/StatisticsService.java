package com.furniture.service;

import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface StatisticsService {
    /**
     * 获取销售统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售统计数据
     */
    Map<String, Object> getSalesStatistics(String startDate, String endDate);
    
    /**
     * 获取产品统计数据
     * @return 产品统计数据
     */
    Map<String, Object> getProductStatistics();
    
    /**
     * 获取用户统计数据
     * @return 用户统计数据
     */
    Map<String, Object> getUserStatistics();
    
    /**
     * 获取分类统计数据
     * @return 分类统计数据
     */
    Map<String, Object> getCategoryStatistics();
    
    /**
     * 获取订单状态统计数据
     * @return 订单状态统计数据
     */
    Map<String, Object> getOrderStatusStatistics();
    
    /**
     * 导出销售统计Excel
     * @param response 响应对象
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @throws IOException 异常
     */
    void exportSalesStatistics(HttpServletResponse response, String startDate, String endDate) throws IOException;
    
    /**
     * 导出产品统计Excel
     * @param response 响应对象
     * @throws IOException 异常
     */
    void exportProductStatistics(HttpServletResponse response) throws IOException;
    
    /**
     * 导出订单统计Excel
     * @param response 响应对象
     * @throws IOException 异常
     */
    void exportOrderStatistics(HttpServletResponse response, @Param("status") Integer status, @Param("startDate") String startDate, @Param("endDate") String endDate) throws IOException;
    
    /**
     * 获取仪表盘统计数据
     * @return 仪表盘统计数据
     */
    Map<String, Object> getDashboardStatistics();
    
    /**
     * 获取产品销量排行
     * @return 产品销量排行数据
     */
    java.util.List<java.util.Map<String, Object>> getProductSalesRanking();
}