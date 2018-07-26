package cn.com.xyc.study.socket.netty.heartbeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClientHeartBeatHandler extends ChannelHandlerAdapter {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?> heartBeat;
    /**
     * 主动向服务器发送认证信息
     */
    private InetAddress addr;

    private static final String SUCCESS_KEY = "auth_success_key";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress();
        String key = "1234";
        String auth = ip + "," + key;
        ctx.writeAndFlush(auth);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof String) {
                String ret = (String) msg;
                if (SUCCESS_KEY.equals(ret)) {
                    //握手成功
                    this.heartBeat = this.scheduler.scheduleWithFixedDelay(new HeartBeatTask(ctx), 0, 2, TimeUnit.SECONDS);
                    System.out.println(msg);
                } else {
                    System.out.println(msg);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    class HeartBeatTask implements Runnable {
        private ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                RequestInfo info = new RequestInfo();
                info.setIp(addr.getHostAddress());

                HashMap<String, Object> cpuMap = new HashMap<>();
                Random random = new Random();
                cpuMap.put("combined", random.nextInt(1000000));
                cpuMap.put("user", random.nextInt(1000000));
                cpuMap.put("sys", random.nextInt(1000000));
                cpuMap.put("wait", random.nextInt(1000000));
                cpuMap.put("idle", random.nextInt(1000000));

                HashMap<String, Object> memMap = new HashMap<>();
                memMap.put("total", random.nextInt(1000000));
                memMap.put("used", random.nextInt(1000000));
                memMap.put("free", random.nextInt(1000000));
                info.setCpuPercMap(cpuMap);
                info.setMemoryMap(memMap);
                ctx.writeAndFlush(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
