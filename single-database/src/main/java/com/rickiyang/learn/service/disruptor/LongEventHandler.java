package com.rickiyang.learn.service.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @author rickiyang
 * @date 2019-06-27
 * @Desc
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        System.out.println(longEvent.getValue());
    }

}
