package com.furniture.service.impl;

import com.furniture.service.StockService;
import com.furniture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class StockServiceImpl implements StockService {
    
    @Autowired
    private ProductService productService;
    
    // 使用内存存储替代Redis
    private Map<String, Integer> stockCache = new ConcurrentHashMap<>();
    private Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    
    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final String LOCK_KEY_PREFIX = "lock:stock:";
    
    @Override
    @Transactional
    public boolean preDeductStock(Integer productId, Integer quantity) {
        String lockKey = LOCK_KEY_PREFIX + productId;
        String stockKey = STOCK_KEY_PREFIX + productId;
        
        ReentrantLock lock = lockMap.computeIfAbsent(lockKey, k -> new ReentrantLock());
        boolean locked = false;
        try {
            locked = lock.tryLock();
        } catch (Exception e) {
            return deductStockFromDb(productId, quantity);
        }
        if (!locked) {
            return deductStockFromDb(productId, quantity);
        }
        
        try {
            Integer stock = stockCache.get(stockKey);
            if (stock == null) {
                com.furniture.entity.Product product = productService.findById(productId);
                if (product == null) {
                    return false;
                }
                stock = product.getStock();
                if (stock == null) stock = 0;
                stockCache.put(stockKey, stock);
            }
            
            if (stock < quantity) {
                return false;
            }
            
            stockCache.put(stockKey, stock - quantity);
            return true;
        } catch (Exception e) {
            return deductStockFromDb(productId, quantity);
        } finally {
            lock.unlock();
            lockMap.remove(lockKey);
        }
    }
    
    @Override
    @Transactional
    public void confirmDeductStock(Integer productId, Integer quantity) {
        // 从数据库扣减库存
        productService.decreaseStock(productId, quantity);
        
        // 更新缓存中的库存
        try {
            String stockKey = STOCK_KEY_PREFIX + productId;
            Integer currentStock = stockCache.get(stockKey);
            if (currentStock != null) {
                stockCache.put(stockKey, currentStock - quantity);
            }
        } catch (Exception e) {
            // 缓存操作失败时，忽略异常
        }
    }
    
    @Override
    public void rollbackStock(Integer productId, Integer quantity) {
        // 回滚缓存中的库存
        try {
            String stockKey = STOCK_KEY_PREFIX + productId;
            Integer currentStock = stockCache.get(stockKey);
            if (currentStock != null) {
                stockCache.put(stockKey, currentStock + quantity);
            }
        } catch (Exception e) {
            // 缓存操作失败时，忽略异常
        }
    }
    
    /**
     * 从数据库扣减库存（降级方案）
     */
    private boolean deductStockFromDb(Integer productId, Integer quantity) {
        com.furniture.entity.Product product = productService.findById(productId);
        if (product == null || product.getStock() == null || product.getStock() < quantity) {
            return false;
        }
        productService.decreaseStock(productId, quantity);
        return true;
    }
}