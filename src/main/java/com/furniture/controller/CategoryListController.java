package com.furniture.controller;

import com.furniture.entity.Category;
import com.furniture.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "分类列表")
@RestController
@RequestMapping("/api/category")
public class CategoryListController {
    
    @Autowired
    private CategoryService categoryService;
    
    @ApiOperation("获取分类列表")
    @GetMapping("/list")
    public List<Category> getCategoryList() {
        return categoryService.findAll();
    }
}