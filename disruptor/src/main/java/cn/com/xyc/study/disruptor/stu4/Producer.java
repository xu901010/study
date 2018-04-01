package cn.com.xyc.study.disruptor.stu4;

import com.lmax.disruptor.RingBuffer;

public class Producer {
    private final RingBuffer<Order> ringBuffer;


    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String data){
        long next = ringBuffer.next();
        try {
            Order order = ringBuffer.get(next);
            order.setId(data);
        }finally {
            ringBuffer.publish(next);
        }
    }
}
