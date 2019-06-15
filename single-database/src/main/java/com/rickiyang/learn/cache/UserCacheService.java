package com.rickiyang.learn.cache;

import com.rickiyang.learn.dao.UserDao;
import com.rickiyang.learn.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: rickiyang
 * @date: 2019/6/15
 * @description: 本地cache
 */
@Service
public class UserCacheService {

    @Resource
    private UserDao userDao;


    /**
     * 查找
     * @param id
     */
    @Cacheable(value = "userCache", key = "#id", sync = true)
    public User getUser(long id){
        //查找数据库
        User user = userDao.selectByPrimaryKey(2L);
        return user;
    }

    /**
     * 保存
     * @param user
     */
    @CachePut(value = "userCache", key = "#user.id")
    public void saveUser(User user){
        //todo 保存数据库
    }


    /**
     * 删除
     * @param user
     */
    @CacheEvict(value = "userCache",key = "#user.id")
    public void delUser(User user){
        //todo 保存数据库
    }
}
