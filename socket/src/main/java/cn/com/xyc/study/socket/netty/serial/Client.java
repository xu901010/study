package cn.com.xyc.study.socket.netty.serial;


import cn.com.xyc.study.socket.util.GzipUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;

public class Client {
    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建Bootstrap
            Bootstrap b = new Bootstrap();
            //指定EventLoopGroup以处理客户端事件，需要用于NIO的实现
            b.group(group)
                    //适用于NIO的传输的Channel类型
                    .channel(NioSocketChannel.class)
                    //在创建Channel时，向ChannelPipeline中添加一个EchoClientHandler实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect("localhost",8765).sync();
            for (int i = 0; i < 1; i++) {
                Req req = new Req();
                req.setId(String.valueOf(i));
                req.setName("pro" + i);
                req.setRequestMessage("数据信息" + i);
                File file = new File("/Users/xuyuancheng/projects/filetest/source/test1.txt");
                FileInputStream in = new FileInputStream(file);
                byte[] data = new byte[in.available()];
                in.read(data);
                in.close();
                req.setAttachment(GzipUtils.gzip(data));
                f.channel().writeAndFlush(req);
            }
            //阻塞直到Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            //关闭线程池并且释放所有的资源
            group.shutdownGracefully();
        }
    }
}
