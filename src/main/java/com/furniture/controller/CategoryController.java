package com.furniture.controller;

import com.furniture.entity.Category;
import com.furniture.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分类管理")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @ApiOperation("获取所有分类")
    @GetMapping
    public List<Category> getCategories() {
        return categoryService.findAll();
    }
    
    @ApiOperation("根据ID查询分类")
    @GetMapping("/{id}")
    public Category getCategoryById(@ApiParam("分类ID") @PathVariable int id) {
        return categoryService.findById(id);
    }
    
    @ApiOperation("添加分类")
    @PostMapping
    public void addCategory(@ApiParam("分类信息") @RequestBody Category category) {
        categoryService.add(category);
    }
    
    @ApiOperation("更新分类")
    @PutMapping("/{id}")
    public void updateCategory(@ApiParam("分类ID") @PathVariable int id, @ApiParam("分类信息") @RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
    }
    
    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public void deleteCategory(@ApiParam("分类ID") @PathVariable int id) {
        categoryService.delete(id);
    }
}