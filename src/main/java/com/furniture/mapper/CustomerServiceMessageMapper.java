package com.furniture.mapper;

import com.furniture.entity.CustomerServiceMessage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CustomerServiceMessageMapper {
    void insert(CustomerServiceMessage message);
    List<CustomerServiceMessage> findByUserId(Integer userId);
    List<CustomerServiceMessage> findAllByUserId(Integer userId);
    List<CustomerServiceMessage> findAll();
    void updateStatus(Integer id, Integer status);
    void updateStatusByUserId(Integer userId, Integer status);
}