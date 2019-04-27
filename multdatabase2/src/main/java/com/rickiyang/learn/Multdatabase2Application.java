package com.rickiyang.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.rickiyang.learn.dao")
@ComponentScan("com.rickiyang.learn")
public class Multdatabase2Application {

    public static void main(String[] args) {
        SpringApplication.run(Multdatabase2Application.class, args);
    }
}
