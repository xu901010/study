package cn.com.xyc.study.disruptor.stu1;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class LongEventMain {
    public static void main(String[] args) {
        //创建工厂
        LongEventFactory factory = new LongEventFactory();
        //创建bufferSize,也就是RingBuffer大小，必须是2的N次方
        int ringBufferSize = 1024 * 1024;
        //创建disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, ringBufferSize, r -> {
            AtomicInteger index = new AtomicInteger(1);
            return new Thread(null, r, "disruptor-thread-" + index.getAndIncrement());
        }, ProducerType.SINGLE, new YieldingWaitStrategy());
        //连接消费方法
        disruptor.handleEventsWith(new LongEventHandler());
        //启动
        disruptor.start();

        //disruptor 的事件发布过程是一个两阶段提交的过程
        //发布事件
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        //LongEventProducer producer = new LongEventProducer(ringBuffer);
        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long l = 0; l < 100; l++) {
            byteBuffer.putLong(0, l);
            producer.onData(byteBuffer);
        }
        //关闭disruptor，方法会堵塞，直到所有的事件都得到处理
        disruptor.shutdown();
    }
}
