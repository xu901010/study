package cn.com.xyc.study.disruptor.stu3;

import cn.com.xyc.study.disruptor.stu2.Trade;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class Handler5 implements EventHandler<Trade>, WorkHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        System.out.println("handler5: get price : " + trade.getPrice());
        trade.setPrice(trade.getPrice() + 3.0);
    }
}
