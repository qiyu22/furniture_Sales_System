package com.furniture.service;

import com.furniture.entity.Category;
import java.util.List;

public interface CategoryService {
    // 获取所有分类
    List<Category> findAll();
    
    // 根据ID查询分类
    Category findById(Integer id);
    
    // 添加分类
    void add(Category category);
    
    // 更新分类
    void update(Category category);
    
    // 删除分类
    void delete(Integer id);
}