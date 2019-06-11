package com.rickiyang.learn.es;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: rickiyang
 * @date: 2019/5/30
 * @description:
 */
@Component
@Log4j2
public class CreateIndexDao {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private EsDao esDao;

    public void createIndexOne() {
        try {
            String index="testdb";  //必须为小写
            String type="userinfo";
            JSONObject obj=new JSONObject();
            obj.put("name","qxw");
            obj.put("age",25);
            obj.put("sex","男");
            String [] tags={"标签1","标签2"};
            obj.put("tags",tags);
            esDao.createIndexOne(index,type,obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
