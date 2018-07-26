package cn.com.xyc.study.socket.netty.serial;

import cn.com.xyc.study.socket.util.GzipUtils;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.FileOutputStream;

public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(" server channel actie ... ");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Req request = (Req) msg;
        System.out.println("Server : " + request.getId() + ", " + request.getName() + ", " + request.getRequestMessage());
        byte[] attachment = GzipUtils.ungzip(request.getAttachment());
        FileOutputStream fos = new FileOutputStream("/Users/xuyuancheng/projects/filetest/target/test1.txt");
        fos.write(attachment);
        fos.close();

        Resp resp = new Resp();
        resp.setId(request.getId());
        resp.setName("resp" + request.getId());
        resp.setResponseMessage("响应内容" + request.getId());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
