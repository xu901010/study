package cn.com.xyc.study.socket.nio.impl2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {
    private static int flag = 1;
    private static SocketChannel socketChannel;

    private static int blockSize = 4096;
    /**
     * 发送数据缓冲区
     */
    private static ByteBuffer sendbuffer = ByteBuffer.allocate(blockSize);
    /**
     * 接收数据缓冲区
     */
    private static ByteBuffer receivebuffer = ByteBuffer.allocate(blockSize);

    /**
     * Socket地址：ip+端口
     */
    private final static InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 1234);

    public static void main(String[] args) throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(serverAddress);

        SocketChannel client;
        Set<SelectionKey> selectionKeys;
        int count;
        String receiveTest, sendText;

        while (true) {
            selector.select();
            selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keys = selectionKeys.iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                if (key.isConnectable()) {
                    System.out.println("client connect");
                    client = (SocketChannel) key.channel();
                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        System.out.println("客户端完成连接操作！");
                        sendbuffer.clear();
                        sendbuffer.put("Hello,Server".getBytes());
                        sendbuffer.flip();
                        client.write(sendbuffer);
                    }
                    client.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    client = (SocketChannel) key.channel();
                    receivebuffer.clear();
                    count = client.read(receivebuffer);
                    if (count > 0) {
                        receiveTest = new String(receivebuffer.array(), 0, count);
                        System.out.println("客户端接收到服务端的数据:" + receiveTest);
                        client.register(selector, SelectionKey.OP_WRITE);
                    }
                } else if (key.isWritable()) {
                    sendbuffer.clear();
                    client = (SocketChannel) key.channel();
                    sendText = "Msg from client--->" + flag++;
                    sendbuffer.put(sendText.getBytes());
                    sendbuffer.flip();
                    client.write(sendbuffer);
                    System.out.println("客户端发送方数据给服务端：" + sendText);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
            selectionKeys.clear();
        }

    }
}
