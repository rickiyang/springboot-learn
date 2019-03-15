package com.rickiyang.demo.dao;

import com.rickiyang.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

public interface UserDao {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}