package com.furniture.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射（classpath打包资源 + 运行期上传的文件系统路径）
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/", "file:src/main/resources/static/images/");
        
        registry.addResourceHandler("/carousel/**")
                .addResourceLocations("classpath:/static/carousel/", "file:src/main/resources/static/carousel/");
        
        // 允许访问静态资源
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}