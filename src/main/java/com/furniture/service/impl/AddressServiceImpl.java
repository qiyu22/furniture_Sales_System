package com.furniture.service.impl;

import com.furniture.entity.Address;
import com.furniture.mapper.AddressMapper;
import com.furniture.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    
    @Autowired
    private AddressMapper addressMapper;
    
    @Override
    public List<Address> findByUserId(Integer userId) {
        return addressMapper.findByUserId(userId);
    }
    
    @Override
    public Address findById(Integer id) {
        return addressMapper.findById(id);
    }
    
    @Override
    public void add(Address address) {
        address.setCreatedAt(new Date());
        address.setUpdatedAt(new Date());
        addressMapper.insert(address);
    }
    
    @Override
    public void update(Address address) {
        address.setUpdatedAt(new Date());
        addressMapper.update(address);
    }
    
    @Override
    public void delete(Integer id) {
        addressMapper.delete(id);
    }
    
    @Override
    public void setDefault(Integer id, Integer userId) {
        // 先将所有地址设置为非默认
        addressMapper.setDefault(userId);
        // 再将指定地址设置为默认
        addressMapper.setDefaultById(id);
    }
}