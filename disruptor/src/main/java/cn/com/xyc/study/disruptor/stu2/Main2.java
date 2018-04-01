package cn.com.xyc.study.disruptor.stu2;

import com.lmax.disruptor.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        int BUFFER_SIZE = 1024;
        int THREAD_NUMBERS = 4;

        RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, BUFFER_SIZE, new YieldingWaitStrategy());

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        WorkerPool<Trade> tradeWorkerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new TradeHandler());
        tradeWorkerPool.start(executor);

        for (int i = 0; i < 8; i++) {
            long req = ringBuffer.next();
            ringBuffer.get(req).setPrice(Math.random() * 9999);
            ringBuffer.publish(req);
        }
        Thread.sleep(1000);
        tradeWorkerPool.halt();
        executor.shutdown();
    }
}
