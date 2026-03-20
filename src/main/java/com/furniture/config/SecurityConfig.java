package com.furniture.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // 禁用CSRF保护
            .authorizeRequests()
            // 允许所有OPTIONS请求
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // 允许公开访问的接口
            .antMatchers("/api/users/login", "/api/users/register").permitAll()
            // 允许Swagger相关路径
            .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs").permitAll()
            // 允许访问静态资源
            .antMatchers("/images/**", "/carousel/**").permitAll()
            // 产品和分类接口，普通用户也可以访问
            .antMatchers("/api/products/**", "/api/category/**").permitAll()
            // 轮播图、评论、统计等公开接口
            .antMatchers("/api/carousel/**", "/api/review/**", "/api/statistics/**").permitAll()
            // 地址接口，需要认证
            .antMatchers("/api/addresses/**").authenticated()
            // 购物车接口，需要认证
            .antMatchers("/api/cart/**").authenticated()
            // 订单接口，需要认证
            .antMatchers("/api/orders/**").authenticated()
            // 管理员接口，需要ADMIN角色
            .antMatchers("/api/admin/**").hasRole("ADMIN")
            // 其他接口需要认证
            .anyRequest().authenticated()
            .and()
            // 表单登录（可选，这里使用自定义登录接口）
            .formLogin().disable()
            // 基本认证（可选）
            .httpBasic().disable();
        
        // 添加JWT过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}