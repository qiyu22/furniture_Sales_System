package com.furniture.controller;

import com.furniture.entity.Product;
import com.furniture.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Api(tags = "产品管理")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @Value("${server.port}")
    private String port;
    
    @ApiOperation("获取产品列表")
    @GetMapping
    public Map<String, Object> getProducts(
            @ApiParam("分类ID") @RequestParam(required = false) Integer categoryId,
            @ApiParam("排序方式") @RequestParam(required = false) String sortBy,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(required = false, defaultValue = "8") Integer size,
            @ApiParam("价格范围") @RequestParam(required = false) Integer priceRange) {
        List<Product> products;
        int total;
        
        if (categoryId != null && categoryId != 0) {
            products = productService.findByCategoryId(categoryId, sortBy, page, size, priceRange);
            total = productService.countByCategoryId(categoryId, priceRange);
        } else {
            products = productService.findAll(sortBy, page, size, priceRange);
            total = productService.countAll(priceRange);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", products);
        result.put("total", total);
        return result;
    }
    
    @ApiOperation("根据ID查询产品")
    @GetMapping("/{id}")
    public Product getProductById(@ApiParam("产品ID") @PathVariable int id) {
        return productService.findById(id);
    }
    
    @ApiOperation("添加产品")
    @PostMapping
    public void addProduct(@ApiParam("产品信息") @RequestBody Product product) {
        productService.save(product);
    }
    
    @ApiOperation("更新产品")
    @PutMapping("/{id}")
    public void updateProduct(@ApiParam("产品ID") @PathVariable int id, @ApiParam("产品信息") @RequestBody Product product) {
        product.setId(id);
        productService.update(product);
    }
    
    @ApiOperation("删除产品")
    @DeleteMapping("/{id}")
    public Object deleteProduct(@ApiParam("产品ID") @PathVariable int id) {
        try {
            productService.delete(id);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "删除成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
            return result;
        }
    }
    
    @ApiOperation("搜索产品")
    @GetMapping("/search")
    public List<Product> searchProducts(@ApiParam("关键词") @RequestParam String keyword) {
        return productService.search(keyword);
    }
    
    @ApiOperation("获取推荐商品")
    @GetMapping("/recommend")
    public List<Product> getRecommendProducts(@ApiParam("用户ID") @RequestParam(required = false) Integer userId) {
        return productService.getRecommendProducts(userId);
    }
    
    @ApiOperation("获取热门商品")
    @GetMapping("/hot")
    public List<Product> getHotProducts() {
        return productService.getHotProducts();
    }

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public Object uploadImage(@ApiParam("图片文件") @RequestParam("file") MultipartFile file) {
        // 确保上传目录存在
        String uploadDir = "D:\\Code_items\\furniture_Sales_System\\src\\main\\resources\\static\\images\\";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null){
            return null;
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;
        String filePath = uploadDir + fileName;

        // 保存文件
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // 构建图片URL
        String imageUrl = "http://localhost:" + port + "/images/" + fileName;

        // 返回结果
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("imageUrl", imageUrl);
        return result;
    }
}