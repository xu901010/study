package cn.com.xyc.netty.echo_client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * SimpleChannelInboundHandler与ChannelInboundHandlerAdaptor的区别是
 * 前者channelRead0方法完成时你已经有了传入消息,并且已经处理完它，当该方法返回时，SimpleChannelInboundHandler负责释放
 * 指向保存该消息的Bytebuf的内存引用
 * 服务器端使用的ChannelInboundHandler。在进行读取消息后，你仍然需要将传入消息回送给发送者，而write()操作是异步的，直到
 * channelRead()方法返回后可能仍然没有完成。为此ChannelInboundHandler扩展了ChannelInboundHandlerAdaptor，其在这个
 * 时间点不会释放消息。消息在EchoServerHandler的channelReadComplete方法中,当writeAndFlush()方法被调用时被释放
 *
 * @Sharable：标签该类可以被多个Channel共享
 */
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知Channel是活跃的时候，发送一条消息
        //当一个连接建立时调用，这样确保了数据将会被尽快地写入服务器
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //记录已接收消息的转储
        /**
         * 每当接收数据时都会调用这个方法，需要注意的是，由服务器发送的消息可能会被分块接收。也就是说服务器发送了5个字节，那么不能保证
         * 这5个字节会被一次性的接收。即使是对于这么少量的数据，channelRead0方法也可能会被调用两次，第一次使用一个持有3个字节的ByteBuf
         * 第二次使用一个持有2个字节的ByteBuf。作为一个面向流的协议，TCP保证了字节数组将会按照服务器发送它们的顺序接收。
         */
        System.out.println("Client received：" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //在发生异常时，记录错误并关闭Channel
        cause.printStackTrace();
        ctx.close();
    }
}
