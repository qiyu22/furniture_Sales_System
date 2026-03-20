package com.furniture.service;

public interface StockService {
    /**
     * 预扣减库存
     * @param productId 产品ID
     * @param quantity 数量
     * @return 是否成功
     */
    boolean preDeductStock(Integer productId, Integer quantity);
    
    /**
     * 确认扣减库存
     * @param productId 产品ID
     * @param quantity 数量
     */
    void confirmDeductStock(Integer productId, Integer quantity);
    
    /**
     * 回滚库存
     * @param productId 产品ID
     * @param quantity 数量
     */
    void rollbackStock(Integer productId, Integer quantity);
}