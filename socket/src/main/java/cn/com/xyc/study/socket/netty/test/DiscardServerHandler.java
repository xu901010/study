package cn.com.xyc.study.socket.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf) msg;
        try {
            while (buf.isReadable()) {
                System.out.print((char) buf.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }*/

        /**
         * 将客户端消息返回给客户端
         * 不同于DISCARD的例子我们并没有释放接受到的消息，这是因为当写入的时候Netty已经帮我们释放了。
         * ctx.write(Object)方法不会使消息写入到通道上，他被缓冲在了内部，你需要调用ctx.flush()方法来把缓冲区中数据强行输出
         */
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
