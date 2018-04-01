package cn.com.xyc.study.disruptor.stu3;

import cn.com.xyc.study.disruptor.stu2.Trade;
import com.lmax.disruptor.EventHandler;

public class Handler2 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        System.out.println("handler2: set price");
        trade.setPrice(17.0);
        Thread.sleep(1000);
    }
}
