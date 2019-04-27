package com.rickiyang.learn.config.dbConfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @Auther: rickiyang
 * @Date: 2019/4/24 23:16
 * @Description:
 */
@Configuration
public class DruidDataSourceConfig {
    /**
     * DataSource 配置
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    @Bean(name = "slaveDruidDataSource")
    public DataSource slaveDruidDataSource() {
        return new DruidDataSource();
    }


    /**
     * DataSource 配置
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    @Primary
    @Bean(name = "masterDruidDataSource")
    public DataSource masterDruidDataSource() {
        return new DruidDataSource();
    }
}

