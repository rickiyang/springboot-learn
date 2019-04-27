package com.rickiyang.learn.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rickiyang.learn.dubbo.OrderDubboService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author yangyue
 * @Date Created in 下午12:55 2019/3/18
 * @Modified by:
 * @Description: dubbo接口实现类
 **/
@Slf4j
@Component
@Service(interfaceClass = OrderDubboService.class)
public class OrderDubboServiceImpl implements OrderDubboService {

    @Override
    public boolean submitOrder(String value) {
        //todo dubbo接口业务逻辑
        return true;
    }
}
