package com.furniture.service.impl;

import com.furniture.entity.Product;
import com.furniture.mapper.ProductMapper;
import com.furniture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Override
    public List<Product> findAll() {
        return productMapper.findAll();
    }
    
    @Override
    public List<Product> findAll(String sortBy) {
        return productMapper.findAllWithSort(sortBy);
    }
    
    @Override
    public List<Product> findByCategoryId(Integer categoryId) {
        return productMapper.findByCategoryId(categoryId);
    }
    
    @Override
    public List<Product> findByCategoryId(Integer categoryId, String sortBy) {
        return productMapper.findByCategoryIdWithSort(categoryId, sortBy);
    }
    
    @Override
    public List<Product> findAll(String sortBy, Integer page, Integer size, Integer priceRange) {
        int offset = (page - 1) * size;
        return productMapper.findAllWithPagination(sortBy, offset, size, priceRange);
    }
    
    @Override
    public int countAll(Integer priceRange) {
        return productMapper.countAll(priceRange);
    }
    
    @Override
    public List<Product> findByCategoryId(Integer categoryId, String sortBy, Integer page, Integer size, Integer priceRange) {
        int offset = (page - 1) * size;
        return productMapper.findByCategoryIdWithPagination(categoryId, sortBy, offset, size, priceRange);
    }
    
    @Override
    public int countByCategoryId(Integer categoryId, Integer priceRange) {
        return productMapper.countByCategoryId(categoryId, priceRange);
    }
    
    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }
    
    @Override
    public void save(Product product) {
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        // 设置默认值
        if (product.getSales() == null) {
            product.setSales(0);
        }
        if (product.getStock() == null) {
            product.setStock(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(1); // 默认上架
        }
        // 如果id为0，则设置为null，让数据库自动生成
        if (product.getId() != null && product.getId() == 0) {
            product.setId(null);
        }
        productMapper.insert(product);
    }
    
    @Override
    public void update(Product product) {
        product.setUpdatedAt(new Date());
        productMapper.update(product);
    }
    
    @Override
    public void delete(Integer id) {
        productMapper.delete(id);
    }
    
    @Override
    public void decreaseStock(Integer productId, Integer quantity) {
        productMapper.decreaseStock(productId, quantity);
    }
    
    @Override
    public void increaseSales(Integer productId, Integer quantity) {
        productMapper.increaseSales(productId, quantity);
    }
    @Override
    public void increaseStock(Integer productId, Integer quantity) {
        productMapper.increaseStock(productId, quantity);
    }

    @Override
    public List<Product> search(String keyword) {
        return productMapper.search(keyword);
    }
    
    @Override
    public List<Product> getRecommendProducts(Integer userId) {
        return productMapper.findRecommendProducts(userId);
    }
    
    @Override
    public List<Product> getHotProducts() {
        return productMapper.findHotProducts();
    }
}