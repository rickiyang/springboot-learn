package com.rickiyang.demo.service;

import com.rickiyang.demo.dao.UserDao;
import com.rickiyang.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void insert(User user){
        userDao.insert(user);
    }

    public User queryUserById(long id){
        return userDao.selectByPrimaryKey(id);
    }


}
