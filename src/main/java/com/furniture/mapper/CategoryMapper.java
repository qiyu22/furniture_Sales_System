package com.furniture.mapper;

import com.furniture.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 查询所有分类
    List<Category> findAll();
    
    // 根据ID查询分类
    Category findById(@Param("id") Integer id);
    
    // 插入分类
    void insert(Category category);
    
    // 更新分类
    void update(Category category);
    
    // 删除分类
    void delete(@Param("id") Integer id);
}