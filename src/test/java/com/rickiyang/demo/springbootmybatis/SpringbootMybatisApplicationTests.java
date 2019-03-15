package com.rickiyang.demo.springbootmybatis;

import com.alibaba.fastjson.JSON;
import com.rickiyang.demo.dao.UserDao;
import com.rickiyang.demo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMybatisApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    public void query() {
        User user = userDao.selectByPrimaryKey(1L);
        System.out.println("=========" + JSON.toJSONString(user));
    }

    @Test
    public void insert() {
        User user = User.builder().age(12).name("xiaoming").sex(1).phone("12345678901").build();
        userDao.insert(user);
    }

}
