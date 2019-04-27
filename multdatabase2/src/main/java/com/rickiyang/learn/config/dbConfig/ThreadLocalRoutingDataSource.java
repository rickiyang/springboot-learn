package com.rickiyang.learn.config.dbConfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Auther: rickiyang
 * @Date: 2019/4/24 23:18
 * @Description:
 */
public class ThreadLocalRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceTypeManager.getDataSource();
    }
}
