package com.furniture.controller;

import com.furniture.entity.Carousel;
import com.furniture.service.CarouselService;
import com.furniture.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carousel")
public class CarouselListController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/list")
    public Result getCarouselList() {
        // 返回启用的轮播图
        List<Carousel> carousels = carouselService.findByStatus(1);
        return Result.success(carousels);
    }
}