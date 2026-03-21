package com.furniture.service.impl;

import com.furniture.mapper.UserFavoriteMapper;
import com.furniture.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteServiceImpl implements UserFavoriteService {
    
    @Autowired
    private UserFavoriteMapper userFavoriteMapper;
    
    @Override
    public void addFavorite(Integer userId, Integer productId) {
        userFavoriteMapper.addFavorite(userId, productId);
    }
    
    @Override
    public void removeFavorite(Integer userId, Integer productId) {
        userFavoriteMapper.removeFavorite(userId, productId);
    }
    
    @Override
    public boolean isFavorite(Integer userId, Integer productId) {
        Integer count = userFavoriteMapper.checkFavorite(userId, productId);
        return count > 0;
    }
    
    @Override
    public List<Integer> getFavoriteProductIds(Integer userId) {
        return userFavoriteMapper.getFavoriteProductIds(userId);
    }
}