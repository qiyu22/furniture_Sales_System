package com.furniture.service.impl;

import com.furniture.entity.CartItem;
import com.furniture.entity.Product;
import com.furniture.entity.ActivityProduct;

import com.furniture.mapper.CartItemMapper;
import com.furniture.service.CartService;
import com.furniture.service.ProductService;
import com.furniture.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ActivityService activityService;
    
    @Override
    public List<CartItem> findByUserId(int userId) {
        List<CartItem> cartItems = cartItemMapper.findByUserId(userId);
        // 加载产品信息
        for (CartItem item : cartItems) {
            Product product = productService.findById(item.getProductId());
            // 加载产品的活动价格
            List<ActivityProduct> activityProducts = activityService.findProductsByProductId(item.getProductId());
            if (!activityProducts.isEmpty()) {
                // 如果有活动价格，设置到产品的活动价格字段
                // 这里简化处理，取第一个活动价格
                // 实际应用中可能需要根据活动类型和时间进行选择
                product.setOriginalPrice(product.getPrice()); // 保存原价
                product.setPrice(activityProducts.get(0).getActivityPrice()); // 设置活动价格
            }
            item.setProduct(product);
        }
        return cartItems;
    }
    
    @Override
    public void addCartItem(int userId, int productId, int quantity) {
        System.out.println("开始添加购物车项: " + userId + ", " + productId + ", " + quantity);
        // 检查购物车中是否已存在该产品
        CartItem existingItem = cartItemMapper.findByUserIdAndProductId(userId, productId);
        System.out.println("检查是否已存在: " + (existingItem != null ? "是" : "否"));
        if (existingItem != null) {
            // 如果已存在，更新数量
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemMapper.updateQuantity(existingItem.getId(), existingItem.getQuantity());
            System.out.println("更新购物车项数量: " + existingItem.getId() + ", " + existingItem.getQuantity());
        } else {
            // 如果不存在，添加新项
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setCreatedAt(new Date());
            cartItem.setUpdatedAt(new Date());
            cartItemMapper.insert(cartItem);
            System.out.println("添加新购物车项: " + cartItem.getUserId() + ", " + cartItem.getProductId());
        }
        System.out.println("添加购物车项完成");
    }
    
    @Override
    public void updateCartItem(int id, int quantity) {
        cartItemMapper.updateQuantity(id, quantity);
    }
    
    @Override
    public void deleteCartItem(int id) {
        cartItemMapper.delete(id);
    }
    
    @Override
    public void clearCart(int userId) {
        cartItemMapper.clearByUserId(userId);
    }
    
    @Override
    public int countByUserId(int userId) {
        return cartItemMapper.countByUserId(userId);
    }
    
    @Override
    public int getCartCount(int userId) {
        return cartItemMapper.countByUserId(userId);
    }
}