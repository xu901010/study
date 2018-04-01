package cn.com.xyc.study.disruptor.stu3;

import cn.com.xyc.study.disruptor.stu2.Trade;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        int bufferSize = 1024;
        Disruptor<Trade> disruptor = new Disruptor<>(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, bufferSize, r -> {
            AtomicInteger atomicInteger = new AtomicInteger(0);
            return new Thread(null, r, "thead-" + atomicInteger.getAndIncrement());
        }, ProducerType.SINGLE, new BusySpinWaitStrategy());
        /*//菱形操作
        EventHandlerGroup<Trade> tradeEventHandlerGroup = disruptor.handleEventsWith(new Handler1(), new Handler2());
        //C1 C2处理后执行JMS消息发送操作，也就是走流程C3
        tradeEventHandlerGroup.then(new Handler3());*/

        /*//顺序操作
        disruptor.handleEventsWith(new Handler1()).handleEventsWith(new Handler2()).handleEventsWith(new Handler3());*/
        //六边形操作
        Handler1 handler1 = new Handler1();
        Handler2 handler2 = new Handler2();
        Handler3 handler3 = new Handler3();
        Handler4 handler4 = new Handler4();
        Handler5 handler5 = new Handler5();
        disruptor.handleEventsWith(handler1,handler2);
        disruptor.after(handler1).handleEventsWith(handler4);
        disruptor.after(handler2).handleEventsWith(handler5);
        disruptor.after(handler4,handler5).handleEventsWith(handler3);



        disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);
        //生产者准备
        new Thread(new TradePublisher(latch, disruptor)).start();
        latch.await();//等待生产结束
        disruptor.shutdown();
        System.out.println("总耗时：" + (System.currentTimeMillis() - beginTime));
    }
}
