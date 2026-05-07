package com.furniture.controller;

import com.furniture.entity.Category;
import com.furniture.service.CategoryService;
import com.furniture.utils.Result;
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
    public Result getCategories() {
        List<Category> categories = categoryService.findAll();
        return Result.success(categories);
    }
    
    @ApiOperation("根据ID查询分类")
    @GetMapping("/{id}")
    public Result getCategoryById(@ApiParam("分类ID") @PathVariable int id) {
        Category category = categoryService.findById(id);
        return Result.success(category);
    }
    
    @ApiOperation("添加分类")
    @PostMapping
    public Result addCategory(@ApiParam("分类信息") @RequestBody Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.error("分类名称不能为空");
        }
        categoryService.add(category);
        return Result.success("添加成功");
    }
    
    @ApiOperation("更新分类")
    @PutMapping("/{id}")
    public Result updateCategory(@ApiParam("分类ID") @PathVariable int id, @ApiParam("分类信息") @RequestBody Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.error("分类名称不能为空");
        }
        category.setId(id);
        categoryService.update(category);
        return Result.success("更新成功");
    }
    
    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public Result deleteCategory(@ApiParam("分类ID") @PathVariable int id) {
        try {
            categoryService.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除失败，该分类下可能存在关联商品");
        }
    }
}