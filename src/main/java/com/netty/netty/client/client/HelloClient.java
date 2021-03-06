package com.netty.netty.client.client;

import com.netty.netty.client.handler.HelloClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class HelloClient {
    private NioSocketChannel channel;
    private CountDownLatch count=new CountDownLatch(1);
    private String host;
    private int port;

    public HelloClient(String host,int port){
        this.host=host;
        this.port=port;
    }

    public void start(){
        // 1、如果现在客户端不同，那么也可以不使用多线程模式来处理;
        // 在Netty中考虑到代码的统一性，也允许你在客户端设置线程池1
        EventLoopGroup group = new NioEventLoopGroup(); // 创建一个线程池
        try {
            Bootstrap client = new Bootstrap(); // 创建客户端处理程序
            client.option(ChannelOption.SO_KEEPALIVE, true);
            client.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true) // 允许接收大块的返回数据
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //socketChannel.pipeline().addLast(new IdleStateHandler(4,0,0,TimeUnit.SECONDS));
//                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
//                            socketChannel.pipeline().addLast(new JSONDecoder());
//                            socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
//                            socketChannel.pipeline().addLast(new JSONEncoder());
                            socketChannel.pipeline().addLast(new HelloClientHandler()); // 追加了处理器
                        }
                    });
            ChannelFuture channelFuture = client.connect("127.0.0.1", 9098);
            channelFuture.sync().addListener(new ReconnectListener(channelFuture));
//            new Thread(()->{
//                try {
//                    count.await();
//                    for(int i =0 ;i<50;i++){
//                        ByteBufAllocator allocator = channel.config().getAllocator();
//                        ByteBuf buffer = allocator.buffer(1024);
//                        buffer.writeBytes(("第"+i+"次发送").getBytes());
//                        channel.pipeline().writeAndFlush(buffer);
//                        TimeUnit.SECONDS.sleep(2);
//                    }
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }).start();
            channel= (NioSocketChannel) channelFuture.channel();
            count.countDown();
            channelFuture.channel().closeFuture().sync(); // 关闭连接
            System.out.println("客户端被关闭");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    class ReconnectListener implements ChannelFutureListener{
        ChannelFuture channelFuture;

        public ReconnectListener(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            //这里是相等的
            System.out.println("future是否相等："+channelFuture.equals(channelFuture));
            System.out.println(future.isDone());
            if(!future.isSuccess()){
                EventLoop loop = future.channel().eventLoop();
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("重连中。。。");
                        HelloClient helloClient = new HelloClient("127.0.0.1", 9999);
                        helloClient.start();
                        System.out.println("重连成功。。。");
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        HelloClient helloClient = new HelloClient("127.0.0.1", 9999);
        helloClient.start();
        //System.out.println(System.getProperty("line.separator"));

    }
}
