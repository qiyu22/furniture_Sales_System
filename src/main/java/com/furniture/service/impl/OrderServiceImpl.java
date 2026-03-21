package com.furniture.service.impl;

import com.furniture.entity.CartItem;
import com.furniture.entity.Order;
import com.furniture.entity.OrderItem;
import com.furniture.entity.Product;
import com.furniture.mapper.OrderMapper;
import com.furniture.mapper.OrderItemMapper;
import com.furniture.service.OrderService;
import com.furniture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private com.furniture.mapper.ProductMapper productMapper;

    @Override
    @Transactional
    public Order createOrder(int userId, List<CartItem> cartItems, String address, String paymentMethod) {
        // 生成订单ID
        String orderId = UUID.randomUUID().toString().replace("-", "");

        // 检查库存
        for (CartItem item : cartItems) {
            Product product = productMapper.findById(item.getProduct().getId());
            if (product == null) {
                throw new RuntimeException("商品不存在: " + item.getProduct().getName());
            }
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + item.getProduct().getName());
            }
        }

        // 计算订单总价
        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 创建订单
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setActualPrice(totalPrice); // 实际支付金额默认为总价
        order.setStatus(0); // 待支付
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(address); // 设置收货地址
        
        // 解析收货地址字符串，提取收货人姓名和联系电话
        if (address != null && !address.isEmpty()) {
            String[] addressParts = address.split(",");
            if (addressParts.length > 0) {
                String[] contactParts = addressParts[0].split(" ");
                if (contactParts.length > 0) {
                    order.setShippingName(contactParts[0]); // 设置收货人姓名
                }
                if (contactParts.length > 1) {
                    order.setShippingPhone(contactParts[1]); // 设置收货人电话
                }
            }
        }
        
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        orderMapper.insert(order);

        // 创建订单详情
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(item.getProduct().getId());
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setProductImage(item.getProduct().getImage());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItemMapper.insert(orderItem);

            // 减少库存
            productService.decreaseStock(item.getProduct().getId(), item.getQuantity());
            // 增加销量
            productService.increaseSales(item.getProduct().getId(), item.getQuantity());

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        return order;
    }

    @Override
    public Order findById(String id) {
        Order order = orderMapper.findById(id);
        if (order != null) {
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(id);
            order.setOrderItems(orderItems);
        }
        return order;
    }

    @Override
    public List<Order> findByUserId(int userId) {
        List<Order> orders = orderMapper.findByUserId(userId);
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderMapper.findAll();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        return orders;
    }

    @Override
    @Transactional
    public void updateStatus(String orderId, int status) {
        // 获取订单信息
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            return;
        }
        
        // 获取当前状态
        int currentStatus = order.getStatus();
        
        // 更新订单状态
        orderMapper.updateStatus(orderId, status);
        
        // 如果订单状态从非取消变为取消，恢复库存和销量
        if (currentStatus != 4 && status == 4) {
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
            for (OrderItem item : orderItems) {
                // 恢复库存
                productService.increaseStock(item.getProductId(), item.getQuantity());
                // 恢复销量
                productService.increaseSales(item.getProductId(), -item.getQuantity());
            }
        } else if (currentStatus == 4 && status != 4) {
            // 如果订单状态从取消变为非取消，重新减少库存和增加销量
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
            for (OrderItem item : orderItems) {
                // 减少库存
                productService.decreaseStock(item.getProductId(), item.getQuantity());
                // 增加销量
                productService.increaseSales(item.getProductId(), item.getQuantity());
            }
        }
    }

    @Override
    @Transactional
    public void update(Order order) {
        // 获取原始订单信息
        Order originalOrder = orderMapper.findById(order.getId());
        int originalStatus = originalOrder.getStatus();
        int newStatus = order.getStatus();
        
        order.setUpdatedAt(new Date());
        orderMapper.update(order);
        
        // 如果订单状态从非取消变为取消，恢复库存和销量
        if (originalStatus != 4 && newStatus == 4) {
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());
            for (OrderItem item : orderItems) {
                // 恢复库存
                productService.increaseStock(item.getProductId(), item.getQuantity());
                // 恢复销量
                productService.increaseSales(item.getProductId(), -item.getQuantity());
            }
        } else if (originalStatus == 4 && newStatus != 4) {
            // 如果订单状态从取消变为非取消，重新减少库存和增加销量
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                for (OrderItem item : order.getOrderItems()) {
                    // 减少库存
                    productService.decreaseStock(item.getProductId(), item.getQuantity());
                    // 增加销量
                    productService.increaseSales(item.getProductId(), item.getQuantity());
                }
            }
        } else if (originalStatus != 4 && newStatus != 4) {
            // 订单状态未变为取消，处理订单项变化
            // 获取原始订单的订单项
            List<OrderItem> originalItems = orderItemMapper.findByOrderId(order.getId());
            
            // 计算原始订单中每个产品的销量
            java.util.Map<Integer, Integer> originalSales = new java.util.HashMap<>();
            for (OrderItem item : originalItems) {
                originalSales.put(item.getProductId(), item.getQuantity());
            }
            
            // 计算新订单中每个产品的销量
            java.util.Map<Integer, Integer> newSales = new java.util.HashMap<>();
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                for (OrderItem item : order.getOrderItems()) {
                    newSales.put(item.getProductId(), item.getQuantity());
                }
            }
            
            // 处理销量和库存变化
            // 1. 处理原始订单中的产品
            for (java.util.Map.Entry<Integer, Integer> entry : originalSales.entrySet()) {
                Integer productId = entry.getKey();
                Integer originalQuantity = entry.getValue();
                Integer newQuantity = newSales.getOrDefault(productId, 0);
                
                if (newQuantity < originalQuantity) {
                    // 销量减少
                    productService.increaseSales(productId, newQuantity - originalQuantity);
                    // 库存增加
                    productService.increaseStock(productId, originalQuantity - newQuantity);
                } else if (newQuantity > originalQuantity) {
                    // 销量增加
                    productService.increaseSales(productId, newQuantity - originalQuantity);
                    // 库存减少
                    productService.decreaseStock(productId, newQuantity - originalQuantity);
                }
                
                // 从新销量映射中移除已处理的产品
                newSales.remove(productId);
            }
            
            // 2. 处理新订单中新增的产品
            for (java.util.Map.Entry<Integer, Integer> entry : newSales.entrySet()) {
                Integer productId = entry.getKey();
                Integer quantity = entry.getValue();
                // 销量增加
                productService.increaseSales(productId, quantity);
                // 库存减少
                productService.decreaseStock(productId, quantity);
            }
        }
        
        // 删除原有的订单项
        orderItemMapper.deleteByOrderId(order.getId());
        
        // 插入新的订单项
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }
        }
    }

    @Override
    public Order findByOrderId(String orderId) {
        return findById(orderId);
    }
}