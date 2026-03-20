package com.furniture.service.impl;

import com.furniture.entity.Category;
import com.furniture.mapper.CategoryMapper;
import com.furniture.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
    
    @Override
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }
    
    @Override
    public void add(Category category) {
        category.setCreatedAt(new Date());
        categoryMapper.insert(category);
    }
    
    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }
    
    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }
}