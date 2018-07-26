package cn.com.xyc.study.socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    /**
     * 缓存线程池
     */
    private ExecutorService executorService;
    /**
     * 创建组
     */
    private AsynchronousChannelGroup threadGroup;
    /**
     * 服务器通道
     */
    public AsynchronousServerSocketChannel assc;

    public Server(int port) throws IOException, InterruptedException {
        //创建一个缓存池
        executorService = Executors.newCachedThreadPool();
        //创建线程组
        threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
        //创建服务器通道
        assc = AsynchronousServerSocketChannel.open(threadGroup);
        //进行绑定
        assc.bind(new InetSocketAddress(port));
        System.out.println("server start , port : " + port);
        //进行阻塞
        assc.accept(this, new ServerCompletionHandler());
        //一直阻塞不让服务器停止
        Thread.sleep(Integer.MAX_VALUE);


    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server(1234);
    }
}
