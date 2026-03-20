package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.furniture"})
@MapperScan("com.furniture.mapper")// 扫描MyBatis Mapper接口
public class SpringbootSalesApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootSalesApplication.class, args);
        
        // 输出Swagger测试地址
        System.out.println("\n==========================================");
        System.out.println("Swagger测试地址: http://localhost:8080/swagger-ui.html");
        System.out.println("==========================================\n");
    }
}