package com.furniture.controller;

import com.furniture.entity.Carousel;
import com.furniture.service.CarouselService;
import com.furniture.utils.Result;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(tags = "轮播图管理")
@RestController
@RequestMapping("/api/carousels")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;
    
    @Value("${server.port}")
    private String port;

    @ApiOperation("获取所有轮播图")
    @GetMapping
    public Result getAllCarousels() {
        List<Carousel> carousels = carouselService.findAll();
        return Result.success(carousels);
    }

    @ApiOperation("根据ID查询轮播图")
    @GetMapping("/{id}")
    public Result getCarouselById(@ApiParam("轮播图ID") @PathVariable int id) {
        Carousel carousel = carouselService.findById(id);
        return Result.success(carousel);
    }

    @ApiOperation("获取启用的轮播图")
    @GetMapping("/status/{status}")
    public Result getCarouselsByStatus(@ApiParam("状态") @PathVariable int status) {
        List<Carousel> carousels = carouselService.findByStatus(status);
        return Result.success(carousels);
    }

    @ApiOperation("添加轮播图")
    @PostMapping
    public Result addCarousel(@ApiParam("轮播图信息") @RequestBody Carousel carousel) {
        carouselService.add(carousel);
        return Result.success("添加成功");
    }

    @ApiOperation("更新轮播图")
    @PutMapping("/{id}")
    public Result updateCarousel(@ApiParam("轮播图ID") @PathVariable int id, @ApiParam("轮播图信息") @RequestBody Carousel carousel) {
        carousel.setId(id);
        carouselService.update(carousel);
        return Result.success("更新成功");
    }

    @ApiOperation("删除轮播图")
    @DeleteMapping("/{id}")
    public Result deleteCarousel(@ApiParam("轮播图ID") @PathVariable int id) {
        carouselService.delete(id);
        return Result.success("删除成功");
    }
    
    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public Result uploadImage(@ApiParam("图片文件") @RequestParam("file") MultipartFile file) {
        // 确保上传目录存在
        String uploadDir = "D:\\Code_items\\furniture_Sales_System\\src\\main\\resources\\static\\carousel";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null){
            return Result.error("文件名为空");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;
        String filePath = uploadDir + File.separator + fileName;

        // 保存文件
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }

        // 构建图片URL
        String imageUrl = "http://localhost:" + port + "/carousel/" + fileName;

        // 返回结果
        Map<String, String> data = new HashMap<>();
        data.put("url", imageUrl);
        return Result.success(data);
    }
}