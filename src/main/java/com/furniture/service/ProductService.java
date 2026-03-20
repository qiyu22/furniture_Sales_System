package com.furniture.service;

import com.furniture.entity.Product;
import java.util.List;

public interface ProductService {
    // 获取所有产品
    List<Product> findAll();
    
    // 获取所有产品（支持排序）
    List<Product> findAll(String sortBy);
    
    // 获取所有产品（支持分页、排序和价格筛选）
    List<Product> findAll(String sortBy, Integer page, Integer size, Integer priceRange);
    
    // 统计所有产品数量（支持价格筛选）
    int countAll(Integer priceRange);
    
    // 根据分类ID查询产品
    List<Product> findByCategoryId(Integer categoryId);
    
    // 根据分类ID查询产品（支持排序）
    List<Product> findByCategoryId(Integer categoryId, String sortBy);
    
    // 根据分类ID查询产品（支持分页、排序和价格筛选）
    List<Product> findByCategoryId(Integer categoryId, String sortBy, Integer page, Integer size, Integer priceRange);
    
    // 统计指定分类的产品数量（支持价格筛选）
    int countByCategoryId(Integer categoryId, Integer priceRange);
    
    // 根据ID查询产品
    Product findById(Integer id);
    
    // 添加产品
    void save(Product product);
    
    // 更新产品
    void update(Product product);
    
    // 删除产品
    void delete(Integer id);
    
    // 减少产品库存
    void decreaseStock(Integer productId, Integer quantity);
    
    // 增加产品销量
    void increaseSales(Integer productId, Integer quantity);

    //增加产品库存
    void increaseStock(Integer productId, Integer quantity);

    // 根据关键词搜索产品
    List<Product> search(String keyword);
    
    // 获取推荐商品
    List<Product> getRecommendProducts(Integer userId);
    
    // 获取热门商品
    List<Product> getHotProducts();
}