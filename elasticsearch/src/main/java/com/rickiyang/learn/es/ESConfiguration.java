package com.rickiyang.learn.es;

import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @author: rickiyang
 * @date: 2019/5/30
 * @description:
 */
@Log4j2
@Configuration
public class ESConfiguration implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    private RestHighLevelClient restHighLevelClient;

    /**
     * 控制Bean的实例化过程
     *
     * @return
     * @throws Exception
     */
    @Override
    public RestHighLevelClient getObject() throws Exception {
        return restHighLevelClient;
    }
    /**
     * 获取接口返回的实例的class
     *
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public void destroy() throws Exception {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            log.error("Error closing ElasticSearch client: ", e);
        }
    }

    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restHighLevelClient = buildClient();
    }

    private RestHighLevelClient buildClient() {
        try {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(clusterNodes.split(":")[0], Integer.parseInt(clusterNodes.split(":")[1]), "http")));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return restHighLevelClient;
    }


}
