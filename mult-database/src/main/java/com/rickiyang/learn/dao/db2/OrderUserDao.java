package com.rickiyang.learn.dao.db2;

import com.rickiyang.learn.entity.db2.OrderUser;

public interface OrderUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(OrderUser record);

    int insertSelective(OrderUser record);

    OrderUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderUser record);

    int updateByPrimaryKey(OrderUser record);
}