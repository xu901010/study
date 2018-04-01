package cn.com.xyc.study.disruptor.stu3;

import cn.com.xyc.study.disruptor.stu2.Trade;
import com.lmax.disruptor.EventHandler;

public class Handler3 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        System.out.println("handler3: name: " + trade.getName() + " , price: " + trade.getPrice() + "; instance: " + trade.toString());
    }
}
