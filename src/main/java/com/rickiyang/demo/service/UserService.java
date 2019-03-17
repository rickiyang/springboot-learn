package com.rickiyang.demo.service;

import com.rickiyang.demo.dao.UserDao;
import com.rickiyang.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author yangyue
 * @Date Created in 下午3:44 2019/3/15
 * @Modified by:
 * @Description:
 **/
@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;

    public void insert(User user){
        userDao.insert(user);
    }

    public User queryUserById(long id){
        return userDao.selectByPrimaryKey(id);
    }

    public void setToRedis(User user){
        if(user == null) {
            return;
        }
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("users",user.getId(),user);
    }


}
