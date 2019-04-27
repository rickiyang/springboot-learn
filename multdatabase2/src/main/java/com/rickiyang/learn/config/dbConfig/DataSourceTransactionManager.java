package com.rickiyang.learn.config.dbConfig;

import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Auther: rickiyang
 * @Date: 2019/4/24 23:15
 * @Description:
 */
@Component
public class DataSourceTransactionManager extends DataSourceTransactionManagerAutoConfiguration {
    @Resource(name = "masterDruidDataSource")
    private DataSource masterDataSource;

    @Bean(name = "transactionManager")

    public org.springframework.jdbc.datasource.DataSourceTransactionManager transactionManagers() {
        return new org.springframework.jdbc.datasource.DataSourceTransactionManager(masterDataSource);
    }

}
