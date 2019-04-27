package com.rickiyang.learn.config.dbConfig;


/**
 * @Auther: rickiyang
 * @Date: 2019/4/24 23:14
 * @Description:
 */
public class DataSourceTypeManager {

    private static final ThreadLocal<String> DATASOURCE = new ThreadLocal<>();

    public static void setDataSource(String dataSourceName) {
        DATASOURCE.set(dataSourceName);
    }

    public static void  clearDataSource() {
        DATASOURCE.remove();
    }

    public static String getDataSource() {
        return DATASOURCE.get();
    }
}
