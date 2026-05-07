package com.furniture.controller;

import com.furniture.entity.Product;
import com.furniture.service.ProductService;
import com.furniture.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result getAllPackages() {
        List<Product> allProducts = productService.findAll();
        // 按分类分组作为套餐
        Map<Integer, List<Product>> grouped = allProducts.stream()
                .filter(p -> p.getCategoryId() != null)
                .collect(Collectors.groupingBy(Product::getCategoryId));
        List<Map<String, Object>> packages = new ArrayList<>();
        for (Map.Entry<Integer, List<Product>> entry : grouped.entrySet()) {
            List<Product> products = entry.getValue();
            if (products.isEmpty()) continue;
            Product first = products.get(0);
            // 计算套餐总价（取商品价格之和）
            double totalPrice = products.stream().mapToDouble(p -> p.getPrice() != null ? p.getPrice().doubleValue() : 0).sum();
            Map<String, Object> pkg = new HashMap<>();
            pkg.put("id", entry.getKey());
            pkg.put("name", first.getName() + " 套餐");
            pkg.put("description", "精选家居组合");
            pkg.put("image", first.getImage());
            pkg.put("price", totalPrice);
            pkg.put("originalPrice", totalPrice * 1.2);
            pkg.put("features", Arrays.asList("免费配送", "品质保证"));
            pkg.put("products", products);
            packages.add(pkg);
        }
        return Result.success(packages);
    }
}
