package cn.com.xyc.study.socket.bio2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(8765);
            System.out.println("server start ...");
            //进行阻塞
            Socket socket = null;
            HandlerExecutorPool executor = new HandlerExecutorPool(100, 1000);
            while (true) {
                socket = server.accept();
                executor.execute(new ServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
