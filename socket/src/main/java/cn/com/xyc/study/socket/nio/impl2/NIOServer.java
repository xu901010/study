package cn.com.xyc.study.socket.nio.impl2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
    private int flag = 1;
    private Selector selector;
    private int blockSize = 4096;
    /**
     * 接收数据缓冲区
     */
    private ByteBuffer receivebuffer = ByteBuffer.allocate(blockSize);
    /**
     * 发送数据缓冲区
     */
    private ByteBuffer sendbuffer = ByteBuffer.allocate(blockSize);

    public NIOServer(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ServerSocket socket = ssc.socket();
        socket.bind(new InetSocketAddress(port));
        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server start ->" + port);
    }

    public void listen() throws IOException {
        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey selectKey = keys.next();
                keys.remove();
                handleKey(selectKey);
            }
        }
    }

    public void handleKey(SelectionKey selectionKey) throws IOException {
        //服务端监听通道
        ServerSocketChannel server = null;
        SocketChannel client = null;
        String reciveText, sendText;
        int count = 0;
        if (selectionKey.isAcceptable()) {
            server = (ServerSocketChannel) selectionKey.channel();
            client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            client = (SocketChannel) selectionKey.channel();
            count = client.read(receivebuffer);
            if (count > 0) {
                reciveText = new String(receivebuffer.array(), 0, count);
                System.out.println("服务端接收到客户端的信息：" + reciveText);
                client.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isWritable()) {
            sendbuffer.clear();
            client = (SocketChannel) selectionKey.channel();
            sendText = "mag send to client:" + flag++;
            sendbuffer.put(sendText.getBytes());
            sendbuffer.flip();
            client.write(sendbuffer);
            System.out.println("服务端发送数据给客户端：" + sendText);
        }
    }

    public static void main(String[] args) throws IOException {
        NIOServer nioServer = new NIOServer(1234);
        nioServer.listen();
    }
}
