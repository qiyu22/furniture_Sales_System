package com.furniture.mapper;

import com.furniture.entity.UserBehavior;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserBehaviorMapper {
    // 插入用户行为
    @Insert("INSERT INTO user_behavior (user_id, product_id, behavior_type, style, created_at) VALUES (#{userId}, #{productId}, #{behaviorType}, #{style}, NOW())")
    int insert(UserBehavior userBehavior);

    // 根据用户ID获取行为记录
    @Select("SELECT * FROM user_behavior WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<UserBehavior> getByUserId(Integer userId);

    // 根据用户ID和行为类型获取记录
    @Select("SELECT * FROM user_behavior WHERE user_id = #{userId} AND behavior_type = #{behaviorType} ORDER BY created_at DESC")
    List<UserBehavior> getByUserIdAndType(Integer userId, String behaviorType);

    // 统计用户对商品的行为次数
    @Select("SELECT COUNT(*) FROM user_behavior WHERE user_id = #{userId} AND product_id = #{productId} AND behavior_type = #{behaviorType}")
    int countByUserIdAndProductIdAndType(Integer userId, Integer productId, String behaviorType);

    // 根据产品ID删除用户行为记录
    @Delete("DELETE FROM user_behavior WHERE product_id = #{productId}")
    int deleteByProductId(Integer productId);
}