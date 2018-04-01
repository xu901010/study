package cn.com.xyc.study.disruptor.stu2;

import com.lmax.disruptor.*;

import java.util.concurrent.*;

public class Main1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int BUFFER_SIZE = 1024;
        int THREAD_NUMBERS = 4;
        /**
         * createSignleProducer创建一个单生产者的RingBuffer.
         * 第一个参数叫EventFactory,从名字上理解就是"事件工厂"，其实它的职责就是产生数据填充RingBuffer的区块。
         * 第二个参数叫RingBuffer的大小，它必须是2的指数倍，目的是为了将求模运算转为&运算提高效率
         * 第三个参数叫RingBuffer的生产在没有可用区块的时候(可能是消费者(或者是事件处理器)太慢了)的等待策略
         */
        RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, BUFFER_SIZE, new YieldingWaitStrategy());

        //创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //创建SequenceBarrier 障碍 ，在消费者和生产者速度不同的时候，使用它可以给消息设置阻塞，等到消费者将消息消费差不多时，障碍再移除
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        //创建消息处理器
        BatchEventProcessor<Trade> tradeBatchEventProcessor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, new TradeHandler());
        //这一步的目的就是把消费者的位置信息注入到生产者，如果只有一个消费者的情况可以省略
        ringBuffer.addGatingSequences(tradeBatchEventProcessor.getSequence());
        //把消息处理器提交到线程池
        executor.submit(tradeBatchEventProcessor);

        //如果存在多个消费者那重复执行上面3行代码把TradeHandler换成其它消费者

        Future<Void> future = executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                long seq;
                for (int i = 0; i < 10; i++) {
                    seq = ringBuffer.next();
                    ringBuffer.get(seq).setPrice(Math.random() + 9999);
                    ringBuffer.publish(seq);
                }
                return null;
            }
        });

        future.get();//等待生产者结束
        Thread.sleep(1000);//等上1秒，等消费都处理完成
        tradeBatchEventProcessor.halt();//通知事件(或者消息)处理器可以结束了(并不是马上结束)
        executor.shutdown();
    }
}
