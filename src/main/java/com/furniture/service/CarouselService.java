package com.furniture.service;

import com.furniture.entity.Carousel;
import java.util.List;

public interface CarouselService {
    // 添加轮播图
    void add(Carousel carousel);
    
    // 更新轮播图
    void update(Carousel carousel);
    
    // 删除轮播图
    void delete(Integer id);
    
    // 根据ID查询轮播图
    Carousel findById(Integer id);
    
    // 获取所有轮播图
    List<Carousel> findAll();
    
    // 获取启用的轮播图
    List<Carousel> findByStatus(Integer status);
}