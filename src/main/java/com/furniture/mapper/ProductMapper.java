package com.furniture.mapper;

import com.furniture.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // 查询所有产品
    List<Product> findAll();
    
    // 查询所有产品（支持排序）
    List<Product> findAllWithSort(@Param("sortBy") String sortBy);
    
    // 根据分类ID查询产品
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId);
    
    // 根据分类ID查询产品（支持排序）
    List<Product> findByCategoryIdWithSort(@Param("categoryId") Integer categoryId, @Param("sortBy") String sortBy);
    
    // 根据ID查询产品
    Product findById(@Param("id") Integer id);
    
    // 插入产品
    void insert(Product product);
    
    // 更新产品
    void update(Product product);
    
    // 删除产品
    void delete(@Param("id") Integer id);
    
    // 减少产品库存
    void decreaseStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
    
    // 增加产品库存
    void increaseStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
    
    // 增加产品销量
    void increaseSales(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
    
    // 根据关键词搜索产品
    List<Product> search(@Param("keyword") String keyword);
    
    // 查询推荐商品
    List<Product> findRecommendProducts(@Param("userId") Integer userId);
    
    // 查询热门商品
    List<Product> findHotProducts();
    
    // 查询所有产品（支持分页、排序和价格筛选）
    List<Product> findAllWithPagination(@Param("sortBy") String sortBy, @Param("offset") int offset, @Param("size") int size, @Param("priceRange") Integer priceRange);
    
    // 统计所有产品数量（支持价格筛选）
    int countAll(@Param("priceRange") Integer priceRange);
    
    // 根据分类ID查询产品（支持分页、排序和价格筛选）
    List<Product> findByCategoryIdWithPagination(@Param("categoryId") Integer categoryId, @Param("sortBy") String sortBy, @Param("offset") int offset, @Param("size") int size, @Param("priceRange") Integer priceRange);
    
    // 统计指定分类的产品数量（支持价格筛选）
    int countByCategoryId(@Param("categoryId") Integer categoryId, @Param("priceRange") Integer priceRange);
}