package com.furniture.service.impl;

import com.furniture.entity.Carousel;
import com.furniture.mapper.CarouselMapper;
import com.furniture.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public void add(Carousel carousel) {
        carousel.setCreatedAt(new Date());
        carousel.setUpdatedAt(new Date());
        carouselMapper.insert(carousel);
    }

    @Override
    public void update(Carousel carousel) {
        carousel.setUpdatedAt(new Date());
        carouselMapper.update(carousel);
    }

    @Override
    public void delete(Integer id) {
        carouselMapper.delete(id);
    }

    @Override
    public Carousel findById(Integer id) {
        return carouselMapper.findById(id);
    }

    @Override
    public List<Carousel> findAll() {
        return carouselMapper.findAll();
    }

    @Override
    public List<Carousel> findByStatus(Integer status) {
        return carouselMapper.findByStatus(status);
    }
}