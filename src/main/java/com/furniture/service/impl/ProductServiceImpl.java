package com.furniture.service.impl;

import com.furniture.entity.Product;
import com.furniture.entity.ActivityProduct;
import com.furniture.mapper.ProductMapper;
import com.furniture.service.ProductService;
import com.furniture.service.RecommendationService;
import com.furniture.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private ActivityService activityService;
    
    @Override
    public List<Product> findAll() {
        List<Product> products = productMapper.findAll();
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    @Override
    public List<Product> findAll(String sortBy) {
        List<Product> products = productMapper.findAllWithSort(sortBy);
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    @Override
    public List<Product> findByCategoryId(Integer categoryId) {
        List<Product> products = productMapper.findByCategoryId(categoryId);
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    @Override
    public List<Product> findByCategoryId(Integer categoryId, String sortBy) {
        List<Product> products = productMapper.findByCategoryIdWithSort(categoryId, sortBy);
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    @Override
    public List<Product> findAll(String sortBy, Integer page, Integer size, Integer priceRange) {
        int offset = (page - 1) * size;
        List<Product> products = productMapper.findAllWithPagination(sortBy, offset, size, priceRange);
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    @Override
    public int countAll(Integer priceRange) {
        return productMapper.countAll(priceRange);
    }
    
    @Override
    public List<Product> findByCategoryId(Integer categoryId, String sortBy, Integer page, Integer size, Integer priceRange) {
        int offset = (page - 1) * size;
        List<Product> products = productMapper.findByCategoryIdWithPagination(categoryId, sortBy, offset, size, priceRange);
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    @Override
    public int countByCategoryId(Integer categoryId, Integer priceRange) {
        return productMapper.countByCategoryId(categoryId, priceRange);
    }
    
    @Override
    public Product findById(Integer id) {
        Product product = productMapper.findById(id);
        if (product != null) {
            // 处理活动价格
            handleActivityPrice(product);
        }
        return product;
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
        // 确保price和originalPrice保持同步
        if (product.getPrice() != null && product.getOriginalPrice() == null) {
            product.setOriginalPrice(product.getPrice());
        } else if (product.getOriginalPrice() != null && product.getPrice() == null) {
            product.setPrice(product.getOriginalPrice());
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
        // 确保price和originalPrice保持同步
        if (product.getPrice() != null && product.getOriginalPrice() == null) {
            product.setOriginalPrice(product.getPrice());
        } else if (product.getOriginalPrice() != null && product.getPrice() == null) {
            product.setPrice(product.getOriginalPrice());
        }
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
        List<Product> products = productMapper.search(keyword);
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    @Override
    public List<Product> getRecommendProducts(Integer userId) {
        // 如果有用户ID，使用个性化推荐
        if (userId != null) {
            List<Product> recommendations = recommendationService.recommendByUserId(userId, 8);
            // 如果个性化推荐没有结果，返回热门商品作为兜底
            if (recommendations != null && !recommendations.isEmpty()) {
                return recommendations;
            }
        }
        // 如果没有用户ID或个性化推荐没有结果，返回热门商品作为推荐
        return productMapper.findHotProducts();
    }
    
    @Override
    public List<Product> getHotProducts() {
        List<Product> products = productMapper.findHotProducts();
        for (Product product : products) {
            handleActivityPrice(product);
        }
        return products;
    }
    
    /**
     * 处理产品的活动价格
     * @param product 产品对象
     */
    private void handleActivityPrice(Product product) {
        try {
            // 重置活动价格，确保活动过期后不显示活动价格
            product.setActivityPrice(null);
            
            // 获取产品的活动信息
            List<ActivityProduct> activityProducts = activityService.findProductsByProductId(product.getId());
            Date now = new Date();
            
            // 遍历活动商品，找到当前有效的活动
            for (ActivityProduct activityProduct : activityProducts) {
                if (activityProduct != null && activityProduct.getActivity() != null) {
                    Date startTime = activityProduct.getActivity().getStartTime();
                    Date endTime = activityProduct.getActivity().getEndTime();
                    
                    // 检查活动是否有效
                    if (startTime != null && endTime != null && now.after(startTime) && now.before(endTime)) {
                        // 活动有效，设置活动价格
                        if (activityProduct.getActivityPrice() != null) {
                            product.setActivityPrice(activityProduct.getActivityPrice());
                        }
                        break; // 只使用第一个有效的活动
                    }
                }
            }
        } catch (Exception e) {
            // 处理异常，确保不影响产品查询
            System.err.println("处理活动价格时出错: " + e.getMessage());
        }
    }
}