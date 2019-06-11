package com.rickiyang.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.rickiyang.learn.dao")
@ImportResource(locations = {"classpath:bean/freemarkerConfig.xml"})
public class SingleDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleDatabaseApplication.class, args);
    }
}
