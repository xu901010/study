package cn.com.xyc.study.disruptor.stu1;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Disruptor3.0提供lambda式的API，这样可以把一些复杂的操作放在RingBuffer。所以 在Disruptor3.0以后最好使用EventPublisher或者 EventTranslator来发布事件
 */
public class LongEventProducerWithTranslator {
    //一个translator可以看作一个事件初始化器，publicEvent方法会调用它
    //填充它
    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
        @Override
        public void translateTo(LongEvent longEvent, long l, ByteBuffer byteBuffer) {
            longEvent.setValue(byteBuffer.getLong(0));
        }
    };

    private final RingBuffer<LongEvent> ringBuffer;


    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer byteBuffer) {
        ringBuffer.publishEvent(TRANSLATOR, byteBuffer);
    }
}
