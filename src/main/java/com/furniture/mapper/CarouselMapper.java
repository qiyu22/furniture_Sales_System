package com.furniture.mapper;

import com.furniture.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarouselMapper {
    // 插入轮播图
    void insert(Carousel carousel);
    
    // 更新轮播图
    void update(Carousel carousel);
    
    // 删除轮播图
    void delete(@Param("id") Integer id);
    
    // 根据ID查询轮播图
    Carousel findById(@Param("id") Integer id);
    
    // 获取所有轮播图
    List<Carousel> findAll();
    
    // 获取启用的轮播图
    List<Carousel> findByStatus(@Param("status") Integer status);
}