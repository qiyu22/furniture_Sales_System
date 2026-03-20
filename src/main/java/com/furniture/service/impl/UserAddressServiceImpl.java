package com.furniture.service.impl;

import com.furniture.entity.UserAddress;
import com.furniture.mapper.UserAddressMapper;
import com.furniture.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> findByUserId(Integer userId) {
        return userAddressMapper.findByUserId(userId);
    }

    @Override
    public void save(UserAddress address) {
        if (address.getId() == null) {
            // 新增地址
            address.setCreatedAt(new Date());
            address.setUpdatedAt(new Date());
            userAddressMapper.insert(address);
        } else {
            // 更新地址
            address.setUpdatedAt(new Date());
            userAddressMapper.update(address);
        }
    }

    @Override
    public void delete(Integer id) {
        userAddressMapper.delete(id);
    }

    @Override
    public void setDefault(Integer id) {
        // 先将用户的所有地址设置为非默认
        UserAddress address = userAddressMapper.findById(id);
        if (address != null) {
            userAddressMapper.resetDefault(address.getUserId());
            // 再将指定地址设置为默认
            userAddressMapper.setDefault(id);
        }
    }

    @Override
    public UserAddress findById(Integer id) {
        return userAddressMapper.findById(id);
    }
}