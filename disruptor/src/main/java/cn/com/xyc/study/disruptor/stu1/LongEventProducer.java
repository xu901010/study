package cn.com.xyc.study.disruptor.stu1;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    /**
     * onData用来发布事件，每调用一次就发布一次事件
     * 它的参数会通过事件传递给消费者
     * @param bb
     */
    public void onData(ByteBuffer bb){
        //可以把ringBuffer看做一个事件队列，那么next就是等得下面一个事件横
        long sequence = ringBuffer.next();
        try {
            //用上面的索引取出一个空的事件用于填充
            LongEvent longEvent = ringBuffer.get(sequence);
            //获取要通过事件传递的业务数据
            longEvent.setValue(bb.getLong(0));
        }finally {
            //发布事件
            //注意，最后的ringBuffer.publish方法必须包含在finally中以确保必须得到调用，如果某个请求的sequence未被提交，将会堵塞后续的发布操作或者其它的producer。
            ringBuffer.publish(sequence);
        }
    }
}
