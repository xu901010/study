package cn.com.xyc.study.socket.netty.runtime;

import cn.com.xyc.study.socket.netty.serial.MarshallingCodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Server {
    public static void main(String[] args) throws Exception {
        //创建EventLoopGroup
        EventLoopGroup pGroup = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        try {
            //创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(pGroup, cGroup)
                    //指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //设置日志
                    .handler(new LoggingHandler(LogLevel.INFO))
                    /**
                     * 添加一个EchoServerHandler到子Channel的ChannelPipeline
                     * 当一个新的连接被接受时，一个新的子Channel将会被创建，而ChannelInitializer将会把一个你的EchoServerHandler的实例添加到该Channel的ChannelPipeline中
                     * 这个ChannelHandler将会收到有关入站消息的通知
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            socketChannel.pipeline().addLast(new ReadTimeoutHandler(5));
                            socketChannel.pipeline().addLast(new ServerHandler());
                        }
                    });
            //异步的绑定服务器;调用sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind(8765).sync();
            //获取Channel的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            //关闭EventLoopGroup释放所有资源，所有所有被创建的线程
            pGroup.shutdownGracefully();
            cGroup.shutdownGracefully();
        }
    }
}
