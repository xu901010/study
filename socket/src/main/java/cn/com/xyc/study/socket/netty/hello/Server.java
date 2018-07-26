package cn.com.xyc.study.socket.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public static void main(String[] args) {
        //一个用于处理服务器端接收客户端连接的
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //一个是进行网络通道的(网络读写的)
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建辅助工具类，用于服务器通道的一系列配置
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            //绑定两个线程组
            bootstrap.group(bossGroup, workerGroup)
                    //指定NIO模式
                    .channel(NioServerSocketChannel.class)
                    //设置tcp缓冲区
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //设置发送缓冲大小
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    //这是接收缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    //保持连接 默认
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //在这里配置具体数据接收方法的处理
                            socketChannel.pipeline().addLast(new ServetHandler());
                        }
                    });
            //进行绑定
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();
            //等待关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
