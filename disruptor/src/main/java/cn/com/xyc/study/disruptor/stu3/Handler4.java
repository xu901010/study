package cn.com.xyc.study.disruptor.stu3;

import cn.com.xyc.study.disruptor.stu2.Trade;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class Handler4 implements EventHandler<Trade>,WorkHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        System.out.println("handler4: get name : "+ trade.getName());
        trade.setName(trade.getName() + "h4");
    }
}
