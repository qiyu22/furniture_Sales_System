package com.furniture.service.impl;

import com.alibaba.excel.EasyExcel;
import com.furniture.entity.*;
import com.furniture.service.StatisticsService;
import com.furniture.service.ProductService;
import com.furniture.service.OrderService;
import com.furniture.service.CategoryService;
import com.furniture.service.UserService;
import com.furniture.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    @Override
    public Map<String, Object> getSalesStatistics(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有订单
        List<Order> orders = orderService.findAll();
        
        // 过滤订单：只统计已完成的订单
        List<Order> completedOrders = orders.stream()
                .filter(order -> order.getStatus() == 3) // 只统计已完成的订单
                .collect(Collectors.toList());
        
        // 计算总销售额
        double totalSales = completedOrders.stream()
                .mapToDouble(order -> order.getTotalPrice().doubleValue())
                .sum();
        
        // 计算今日销售额
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        double todaySales = completedOrders.stream()
                .filter(order -> dateFormat.format(order.getCreatedAt()).equals(today))
                .mapToDouble(order -> order.getTotalPrice().doubleValue())
                .sum();
        
        // 计算本月销售额
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        String currentMonth = monthFormat.format(new Date()); // 格式：yyyy-MM
        double monthSales = completedOrders.stream()
                .filter(order -> monthFormat.format(order.getCreatedAt()).equals(currentMonth))
                .mapToDouble(order -> order.getTotalPrice().doubleValue())
                .sum();
        
        // 按日期分组统计
        Map<String, Double> salesByDate = completedOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> dateFormat.format(order.getCreatedAt()),
                        Collectors.summingDouble(order -> order.getTotalPrice().doubleValue())
                ));
        
        // 按日期分组统计订单数量
        Map<String, Long> ordersByDate = completedOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> dateFormat.format(order.getCreatedAt()),
                        Collectors.counting()
                ));
        
        // 按产品分组统计销售额
        Map<String, Double> salesByProduct = new HashMap<>();
        // 按产品分组统计销量
        Map<String, Integer> salesQuantityByProduct = new HashMap<>();
        
        completedOrders.forEach(order -> {
            if (order.getOrderItems() != null) {
                order.getOrderItems().forEach(item -> {
                    // 使用OrderItem中的productName字段，而不是关联的Product对象
                    String productName = item.getProductName();
                    double sales = item.getPrice().doubleValue() * item.getQuantity();
                    int quantity = item.getQuantity();
                    
                    salesByProduct.put(productName, salesByProduct.getOrDefault(productName, 0.0) + sales);
                    salesQuantityByProduct.put(productName, salesQuantityByProduct.getOrDefault(productName, 0) + quantity);
                });
            }
        });
        
        // 计算订单统计
        int totalOrders = orders.size();
        int todayOrders = (int) orders.stream()
                .filter(order -> dateFormat.format(order.getCreatedAt()).equals(today))
                .count();
        int monthOrders = (int) orders.stream()
                .filter(order -> monthFormat.format(order.getCreatedAt()).equals(currentMonth))
                .count();
        int completedOrderCount = completedOrders.size();
        
        // 计算最近7天和30天的订单数
        Map<String, Long> last7DaysOrders = new HashMap<>();
        Map<String, Long> last30DaysOrders = new HashMap<>();
        
        // 按分类统计销量
        Map<String, Integer> salesByCategory = new HashMap<>();
        
        completedOrders.forEach(order -> {
            if (order.getOrderItems() != null) {
                order.getOrderItems().forEach(item -> {
                    // 这里简化处理，实际应该根据产品ID获取分类
                    // 暂时使用硬编码的分类
                    String category = "其他";
                    if (item.getProductName().contains("沙发") || item.getProductName().contains("茶几") || item.getProductName().contains("电视柜")) {
                        category = "客厅家具";
                    } else if (item.getProductName().contains("床") || item.getProductName().contains("衣柜") || item.getProductName().contains("床头柜")) {
                        category = "卧室家具";
                    } else if (item.getProductName().contains("书桌") || item.getProductName().contains("办公椅") || item.getProductName().contains("文件柜")) {
                        category = "办公家具";
                    }
                    
                    int quantity = item.getQuantity();
                    salesByCategory.put(category, salesByCategory.getOrDefault(category, 0) + quantity);
                });
            }
        });
        
        result.put("totalSales", totalSales);
        result.put("todaySales", todaySales);
        result.put("monthSales", monthSales);
        result.put("salesByDate", salesByDate);
        result.put("ordersByDate", ordersByDate);
        result.put("salesByProduct", salesByProduct);
        result.put("salesQuantityByProduct", salesQuantityByProduct);
        result.put("totalOrders", totalOrders);
        result.put("todayOrders", todayOrders);
        result.put("monthOrders", monthOrders);
        result.put("completedOrderCount", completedOrderCount);
        result.put("last7DaysOrders", last7DaysOrders);
        result.put("last30DaysOrders", last30DaysOrders);
        result.put("salesByCategory", salesByCategory);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getProductStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有产品
        List<Product> products = productService.findAll();
        
        // 计算产品总数
        int totalProducts = products.size();
        
        // 按分类分组统计
        Map<String, Long> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        product -> {
                            Category category = categoryService.findById(product.getCategoryId());
                            return category != null ? category.getName() : "未知分类";
                        },
                        Collectors.counting()
                ));
        
        // 计算总库存
        int totalStock = products.stream()
                .mapToInt(product -> product.getStock() != null ? product.getStock() : 0)
                .sum();
        
        // 计算低库存商品（库存小于10）
        int lowStockProducts = (int) products.stream()
                .filter(product -> (product.getStock() != null && product.getStock() < 10))
                .count();
        
        // 计算缺货商品（库存为0）
        int outOfStockProducts = (int) products.stream()
                .filter(product -> (product.getStock() != null && product.getStock() == 0))
                .count();
        
        // 按销量排序的产品列表（TOP10）
        List<Map<String, Object>> topSellingProducts = products.stream()
                .map(product -> {
                    Map<String, Object> productInfo = new HashMap<>();
                    productInfo.put("name", product.getName());
                    productInfo.put("sales", product.getSales() != null ? product.getSales() : 0);
                    productInfo.put("price", product.getPrice());
                    productInfo.put("stock", product.getStock() != null ? product.getStock() : 0);
                    return productInfo;
                })
                .sorted((a, b) -> Integer.compare((int) b.get("sales"), (int) a.get("sales")))
                .limit(10)
                .collect(Collectors.toList());
        
        // 商品评分统计（使用真实数据）
        Map<String, Object> ratingStatistics = new HashMap<>();
        
        // 计算平均评分
        double averageRating = products.stream()
                .filter(product -> product.getRating() != null)
                .mapToDouble(product -> product.getRating().doubleValue())
                .average()
                .orElse(0.0);
        
        // 计算好评率（评分大于等于4的产品占比）
        long totalRatedProducts = products.stream()
                .filter(product -> product.getRating() != null)
                .count();
        long positiveProducts = products.stream()
                .filter(product -> product.getRating() != null && product.getRating().doubleValue() >= 4)
                .count();
        double positiveRate = totalRatedProducts > 0 ? (double) positiveProducts / totalRatedProducts : 0.0;
        
        // 计算各星级的占比
        long fiveStar = products.stream()
                .filter(product -> product.getRating() != null && product.getRating().doubleValue() == 5)
                .count();
        long fourStar = products.stream()
                .filter(product -> product.getRating() != null && product.getRating().doubleValue() == 4)
                .count();
        long threeStar = products.stream()
                .filter(product -> product.getRating() != null && product.getRating().doubleValue() == 3)
                .count();
        long twoStar = products.stream()
                .filter(product -> product.getRating() != null && product.getRating().doubleValue() == 2)
                .count();
        long oneStar = products.stream()
                .filter(product -> product.getRating() != null && product.getRating().doubleValue() == 1)
                .count();
        
        // 转换为百分比
        int fiveStarPercent = totalRatedProducts > 0 ? (int) (fiveStar * 100 / totalRatedProducts) : 0;
        int fourStarPercent = totalRatedProducts > 0 ? (int) (fourStar * 100 / totalRatedProducts) : 0;
        int threeStarPercent = totalRatedProducts > 0 ? (int) (threeStar * 100 / totalRatedProducts) : 0;
        int twoStarPercent = totalRatedProducts > 0 ? (int) (twoStar * 100 / totalRatedProducts) : 0;
        int oneStarPercent = totalRatedProducts > 0 ? (int) (oneStar * 100 / totalRatedProducts) : 0;
        
        ratingStatistics.put("averageRating", Math.round(averageRating * 10) / 10.0); // 保留一位小数
        ratingStatistics.put("positiveRate", Math.round(positiveRate * 100) / 100.0); // 保留两位小数
        ratingStatistics.put("fiveStar", fiveStarPercent);
        ratingStatistics.put("fourStar", fourStarPercent);
        ratingStatistics.put("threeStar", threeStarPercent);
        ratingStatistics.put("twoStar", twoStarPercent);
        ratingStatistics.put("oneStar", oneStarPercent);
        
        result.put("totalProducts", totalProducts);
        result.put("productsByCategory", productsByCategory);
        result.put("totalStock", totalStock);
        result.put("lowStockProducts", lowStockProducts);
        result.put("outOfStockProducts", outOfStockProducts);
        result.put("topSellingProducts", topSellingProducts);
        result.put("ratingStatistics", ratingStatistics);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 这里应该实现用户统计逻辑
        // 简化实现，实际应该从数据库查询
        result.put("totalUsers", 100);
        result.put("newUsersToday", 5);
        result.put("activeUsers", 80);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getCategoryStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有分类
        List<Category> categories = categoryService.findAll();
        
        // 计算分类总数
        int totalCategories = categories.size();
        
        // 按分类统计产品数量
        Map<String, Long> productCountByCategory = new HashMap<>();
        List<Product> products = productService.findAll();
        products.forEach(product -> {
            Category category = categoryService.findById(product.getCategoryId());
            if (category != null) {
                String categoryName = category.getName();
                productCountByCategory.put(categoryName, productCountByCategory.getOrDefault(categoryName, 0L) + 1);
            }
        });
        
        result.put("totalCategories", totalCategories);
        result.put("productCountByCategory", productCountByCategory);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getOrderStatusStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有订单
        List<Order> orders = orderService.findAll();
        
        // 按状态分组统计
        Map<String, Long> orderStatusCount = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> {
                            switch (order.getStatus()) {
                                case 0: return "待支付";
                                case 1: return "已支付";
                                case 2: return "已发货";
                                case 3: return "已完成";
                                case 4: return "已取消";
                                default: return "未知";
                            }
                        },
                        Collectors.counting()
                ));
        
        result.put("orderStatusCount", orderStatusCount);
        
        return result;
    }
    
    @Override
    public void exportSalesStatistics(HttpServletResponse response, String startDate, String endDate) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("销售统计报表_" + new Date().toString(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        
        // 获取销售统计数据
        Map<String, Object> salesData = getSalesStatistics(startDate, endDate);
        
        // 准备导出数据
        List<List<Object>> exportData = new ArrayList<>();
        
        // 添加销售数据
        if (salesData.containsKey("salesByDate")) {
            Map<String, Double> salesByDate = (Map<String, Double>) salesData.get("salesByDate");
            Map<String, Long> ordersByDate = (Map<String, Long>) salesData.get("ordersByDate");
            
            salesByDate.forEach((date, sales) -> {
                Long orderCount = ordersByDate.getOrDefault(date, 0L);
                double avg = orderCount > 0 ? sales / orderCount : 0;
                
                exportData.add(Arrays.asList(
                        date,
                        orderCount,
                        sales,
                        avg
                ));
            });
        }
        
        System.out.println("导出销售数据数量：" + exportData.size());
        
        // 导出Excel
        EasyExcel.write(response.getOutputStream())
                .head(Arrays.asList(
                        Arrays.asList("日期"),
                        Arrays.asList("订单数量"),
                        Arrays.asList("销售额"),
                        Arrays.asList("平均订单金额")
                ))
                .sheet("销售统计")
                .doWrite(exportData);
    }
    
    @Override
    public void exportProductStatistics(HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("商品销售报表_" + new Date().toString(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        
        // 获取所有产品
        List<Product> products = productService.findAll();
        
        // 准备导出数据
        List<List<Object>> exportData = new ArrayList<>();
        
        // 添加产品数据
        for (Product product : products) {
            // 获取分类名称
            Category category = categoryService.findById(product.getCategoryId());
            String categoryName = category != null ? category.getName() : "未知";
            
            exportData.add(Arrays.asList(
                    product.getId(),
                    product.getName(),
                    categoryName,
                    product.getPrice(),
                    product.getSales(),
                    product.getStock(),
                    product.getPrice().multiply(new java.math.BigDecimal(product.getSales()))
            ));
        }
        
        System.out.println("导出商品数据数量：" + exportData.size());
        
        // 导出Excel
        EasyExcel.write(response.getOutputStream())
                .head(Arrays.asList(
                        Arrays.asList("商品ID"),
                        Arrays.asList("商品名称"),
                        Arrays.asList("分类"),
                        Arrays.asList("商品价格"),
                        Arrays.asList("销量"),
                        Arrays.asList("库存"),
                        Arrays.asList("销售额")
                ))
                .sheet("商品销售")
                .doWrite(exportData);
    }
    
    @Override
    public void exportOrderStatistics(HttpServletResponse response, Integer status, String startDate, String endDate) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("订单报表_" + new Date().toString(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        
        // 获取所有订单
        List<Order> orders = orderService.findAll();
        
        // 筛选订单
        List<Order> filteredOrders = orders.stream()
                .filter(order -> {
                    // 状态筛选
                    if (status != null && order.getStatus() != status) {
                        return false;
                    }
                    // 日期筛选
                    if (startDate != null || endDate != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String orderDate = dateFormat.format(order.getCreatedAt());
                        if (startDate != null && orderDate.compareTo(startDate) < 0) {
                            return false;
                        }
                        if (endDate != null && orderDate.compareTo(endDate) > 0) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
        
        // 准备导出数据
        List<List<Object>> exportData = new ArrayList<>();
        
        // 添加订单数据
        for (Order order : filteredOrders) {
            // 获取订单中的商品
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());
            
            for (OrderItem item : orderItems) {
                // 获取用户名
                User user = userService.findById(order.getUserId());
                String username = user != null ? user.getUsername() : "未知";
                
                // 获取商品名称
                Product product = productService.findById(item.getProductId());
                String productName = product != null ? product.getName() : "未知";
                
                // 订单状态
                String statusText = "";
                switch (order.getStatus()) {
                    case 0: statusText = "待支付"; break;
                    case 1: statusText = "已支付"; break;
                    case 2: statusText = "已发货"; break;
                    case 3: statusText = "已完成"; break;
                    case 4: statusText = "已取消"; break;
                    default: statusText = "未知"; break;
                }
                
                exportData.add(Arrays.asList(
                        order.getId(),
                        order.getUserId(),
                        username,
                        order.getId(),
                        productName,
                        item.getQuantity(),
                        item.getPrice(),
                        order.getTotalPrice(),
                        order.getActualPrice(),
                        statusText,
                        order.getShippingAddress(),
                        order.getCreatedAt(),
                        order.getPaymentTime()
                ));
            }
        }
        
        System.out.println("导出订单数据数量：" + exportData.size());
        
        // 导出Excel
        EasyExcel.write(response.getOutputStream())
                .head(Arrays.asList(
                        Arrays.asList("订单ID"),
                        Arrays.asList("用户ID"),
                        Arrays.asList("用户名"),
                        Arrays.asList("订单编号"),
                        Arrays.asList("商品名称"),
                        Arrays.asList("商品数量"),
                        Arrays.asList("商品单价"),
                        Arrays.asList("订单总金额"),
                        Arrays.asList("支付金额"),
                        Arrays.asList("订单状态"),
                        Arrays.asList("收货地址"),
                        Arrays.asList("创建时间"),
                        Arrays.asList("支付时间")
                ))
                .sheet("订单报表")
                .doWrite(exportData);
    }
    
    @Override
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取所有订单
            List<Order> orders = orderService.findAll();
            // 获取所有产品
            List<Product> products = productService.findAll();
            
            // 计算总销售额
            double totalSales = orders.stream()
                    .filter(order -> order.getStatus() == 3 && order.getTotalPrice() != null) // 只统计已完成的订单
                    .mapToDouble(order -> order.getTotalPrice().doubleValue())
                    .sum();
            
            // 计算总订单数
            int totalOrders = orders.size();
            
            // 计算总商品数
            int totalProducts = products.size();
            
            // 计算总用户数（排除管理员）
            List<User> users = userService.findAll();
            int totalUsers = (int) users.stream()
                    .filter(user -> user.getRole() != null && !"ADMIN".equals(user.getRole()))
                    .count();
            
            // 销售趋势（最近7天）
            Map<String, Object> salesTrend = new HashMap<>();
            List<String> dates = new ArrayList<>();
            List<Double> values = new ArrayList<>();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            for (int i = 6; i >= 0; i--) {
                Date date = new Date(today.getTime() - i * 24 * 60 * 60 * 1000);
                String dateStr = dateFormat.format(date);
                dates.add(dateStr);
                
                // 计算当天销售额
                double daySales = orders.stream()
                        .filter(order -> order.getStatus() == 3 && order.getCreatedAt() != null && order.getTotalPrice() != null && dateFormat.format(order.getCreatedAt()).equals(dateStr))
                        .mapToDouble(order -> order.getTotalPrice().doubleValue())
                        .sum();
                values.add(daySales);
            }
            salesTrend.put("dates", dates);
            salesTrend.put("values", values);
            
            // 订单状态分布
            List<Map<String, Object>> orderStatus = new ArrayList<>();
            Map<Integer, Long> statusCount = orders.stream()
                    .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
            
            Map<Integer, String> statusMap = new HashMap<>();
            statusMap.put(0, "待支付");
            statusMap.put(1, "已支付");
            statusMap.put(2, "已发货");
            statusMap.put(3, "已完成");
            statusMap.put(4, "已取消");
            
            statusCount.forEach((status, count) -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", statusMap.getOrDefault(status, "未知"));
                item.put("value", count);
                orderStatus.add(item);
            });
            
            // 热门商品（销量前5）
            List<Map<String, Object>> topProducts = products.stream()
                    .map(product -> {
                        Map<String, Object> productInfo = new HashMap<>();
                        productInfo.put("name", product.getName() != null ? product.getName() : "未知商品");
                        productInfo.put("sales", product.getSales() != null ? product.getSales() : 0);
                        productInfo.put("price", product.getPrice());
                        return productInfo;
                    })
                    .sorted((a, b) -> Integer.compare((int) b.get("sales"), (int) a.get("sales")))
                    .limit(5)
                    .collect(Collectors.toList());
            
            // 最近订单（最近5个）
            List<Map<String, Object>> recentOrders = orders.stream()
                    .filter(order -> order.getCreatedAt() != null)
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .limit(5)
                    .map(order -> {
                        Map<String, Object> orderInfo = new HashMap<>();
                        orderInfo.put("id", order.getId());
                        orderInfo.put("totalPrice", order.getTotalPrice());
                        orderInfo.put("status", order.getStatus());
                        orderInfo.put("createdAt", order.getCreatedAt());
                        return orderInfo;
                    })
                    .collect(Collectors.toList());
            
            result.put("totalSales", totalSales);
            result.put("totalOrders", totalOrders);
            result.put("totalProducts", totalProducts);
            result.put("totalUsers", totalUsers);
            result.put("salesTrend", salesTrend);
            result.put("orderStatus", orderStatus);
            result.put("topProducts", topProducts);
            result.put("recentOrders", recentOrders);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果出现异常，返回默认值
            result.put("totalSales", 0.0);
            result.put("totalOrders", 0);
            result.put("totalProducts", 0);
            result.put("totalUsers", 0);
            Map<String, Object> salesTrendDefault = new HashMap<>();
            salesTrendDefault.put("dates", new ArrayList<String>());
            salesTrendDefault.put("values", new ArrayList<Double>());
            result.put("salesTrend", salesTrendDefault);
            result.put("orderStatus", new ArrayList<Map<String, Object>>());
            result.put("topProducts", new ArrayList<Map<String, Object>>());
            result.put("recentOrders", new ArrayList<Map<String, Object>>());
        }
        
        return result;
    }
    
    @Override
    public java.util.List<java.util.Map<String, Object>> getProductSalesRanking() {
        // 获取所有产品
        List<Product> products = productService.findAll();
        
        // 按销量排序，返回前5个
        return products.stream()
                .map(product -> {
                    java.util.Map<String, Object> productInfo = new java.util.HashMap<>();
                    productInfo.put("id", product.getId());
                    productInfo.put("name", product.getName());
                    productInfo.put("price", product.getPrice());
                    productInfo.put("sales", product.getSales());
                    return productInfo;
                })
                .sorted((a, b) -> Integer.compare((int) b.get("sales"), (int) a.get("sales")))
                .limit(5)
                .collect(java.util.stream.Collectors.toList());
    }
}