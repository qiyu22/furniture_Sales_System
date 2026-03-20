package com.furniture.controller;

import com.furniture.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "最新评价")
@RestController
@RequestMapping("/api/review")
public class ReviewLatestController {

    @Autowired
    private ReviewService reviewService;

    @ApiOperation("获取最新评价")
    @GetMapping("/latest")
    public List<Map<String, Object>> getLatestReviews() {
        return reviewService.findLatestWithProductName();
    }
}