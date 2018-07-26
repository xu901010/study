package cn.com.xyc.study.socket.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        /**
         * ChannelHandlerContext.write()(和writeAndFlush())方法会返回一个ChannelFuture对象，一个ChannelFuture代表了一个还没有发生的I/O操作。
         * 这意味着任何一个请求操作都不会马上被执行，因为在Netty里所有的操作都是异步的
         */
        final ChannelFuture f = ctx.writeAndFlush(time);
        /**
         * 当一个写请求已经完成是如何通知到我们？这个只需要简单地在返回的ChannelFuture上增加一个ChannelFutureListene
         * 或者，你可以使用简单的预定义监听器代码 f.addListener(ChannelFutureListener.CLOSE);
         */
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                assert f == channelFuture;
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
