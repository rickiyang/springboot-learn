package com.rickiyang.learn.config;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;


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

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restHighLevelClient = buildClient();
    }

    private RestHighLevelClient buildClient() {
        try {
            List<String> hostList = Lists.newArrayList(clusterNodes.split(","));
            HttpHost[] httpHosts = new HttpHost[hostList.size()];
            for (int i = 0; i < hostList.size(); i++) {
                String hostStr = hostList.get(i);
                String[] split = hostStr.split(":");
                HttpHost httpHost = new HttpHost(split[0], Integer.valueOf(split[1]), "http");
                httpHosts[i] = httpHost;
            }
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHosts));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return restHighLevelClient;
    }


}
