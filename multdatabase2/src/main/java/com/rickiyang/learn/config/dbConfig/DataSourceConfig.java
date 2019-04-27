package com.rickiyang.learn.config.dbConfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: rickiyang
 * @Date: 2019/4/24 23:13
 * @Description:
 */
@Configuration
public class DataSourceConfig {
   public static final  String  SLAVE = "slaveDruidDataSource";
   public static final  String  MASTER = "masterDruidDataSource";

   @Resource(name = "masterDruidDataSource")
   private DataSource masterDataSource;

   @Resource(name = "slaveDruidDataSource")
   private DataSource slaveDataSource;

   /**
    * AbstractRoutingDataSource
    * @return
    * @throws Exception
    */
   @Bean
   public AbstractRoutingDataSource routingDataSource() throws Exception {
      ThreadLocalRoutingDataSource routing = new ThreadLocalRoutingDataSource();
      Map<Object,Object> targetDataSource = new HashMap<>(4);
      targetDataSource.put(SLAVE, slaveDataSource);
      targetDataSource.put(MASTER, masterDataSource);
      routing.setTargetDataSources(targetDataSource);
      routing.setDefaultTargetDataSource(slaveDataSource);
      return routing;
   }

   /**
    * SqlSessionFactory
    * @return
    * @throws Exception
    */
   @Bean
   public SqlSessionFactory sqlSessionFactory() throws Exception {
      SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
      sqlSessionFactoryBean.setDataSource(routingDataSource());

      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*/*.xml"));

      sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(false);

      return sqlSessionFactoryBean.getObject();
   }
}
