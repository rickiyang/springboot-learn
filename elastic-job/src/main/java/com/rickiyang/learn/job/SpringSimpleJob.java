package com.rickiyang.learn.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

@Component
public class SpringSimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext context) {
        switch (context.getShardingItem()) {
            case 0:
                System.out.println("SpringSimpleJob：This is my Case 0 Test! This is My 0 Service!");
                break;
            case 1:
                System.out.println("SpringSimpleJob：This is my Case 1 Test! This is My 1 Service!");
                break;
            case 2:
                System.out.println("SpringSimpleJob：This is my Case 2 Test!");
                break;
            // case n: ...
        }
    }
}
