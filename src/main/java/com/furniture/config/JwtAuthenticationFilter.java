package com.furniture.config;

import com.furniture.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 放行OPTIONS请求
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        // 从请求头中获取token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                username = jwtUtils.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error("Error extracting username from token: " + e.getMessage());
            }
        }
        
        // 验证token并设置认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtUtils.validateToken(token)) {
                    Claims claims = jwtUtils.parseToken(token);
                    String role = (String) claims.get("role");
                    Object userId = claims.get("userId");
                    
                    // 创建用户详情，包含角色信息
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    if (role != null) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                    }
                    
                    UserDetails userDetails = User.builder()
                            .username(username)
                            .password("")
                            .authorities(authorities)
                            .build();
                    
                    // 创建认证令牌
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 设置认证信息到安全上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    // 将用户信息存储到请求属性中，方便后续使用
                    request.setAttribute("username", username);
                    request.setAttribute("role", role);
                    if (userId != null) {
                        request.setAttribute("userId", userId);
                    }
                } else {
                    // Token 无效，返回 401 错误
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Token expired or invalid\"}");
                    return;
                }
            } catch (Exception e) {
                logger.error("Error processing JWT token: " + e.getMessage());
                // 清除安全上下文
                SecurityContextHolder.clearContext();
                // 返回 401 错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token expired or invalid\"}");
                return;
            }
        }
        
        // 检查是否是需要认证的接口
        String requestUri = request.getRequestURI();
        boolean requiresAuth = false;
        
        // 检查是否是需要认证的接口
        if (requestUri.startsWith("/api/addresses/") ||
            requestUri.startsWith("/api/cart/") ||
            requestUri.startsWith("/api/orders/") ||
            requestUri.startsWith("/api/admin/") ||
            requestUri.startsWith("/api/users/info")) {
            requiresAuth = true;
        }
        
        // 如果需要认证但没有token，返回401错误
        if (requiresAuth && username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication required\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}