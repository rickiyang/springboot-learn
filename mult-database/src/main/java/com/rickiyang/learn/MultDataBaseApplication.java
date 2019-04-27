package com.rickiyang.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MultDataBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultDataBaseApplication.class, args);
    }
}
