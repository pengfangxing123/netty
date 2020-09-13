package com.netty.netty.clientandserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 服务端又是客户端
 * 这是一种思路，把netty的channel 注册到两个eventLoop上 ，分别监听不同的事件，减少eventLoop的压力
 * 理论支持是，一个jdk的channel 可以注册到不同的selector上
 * 但是在netty中一个netty 的channel只能注册到一个eventLoop，
 * 在{@link AbstractChannel register(EventLoop eventLoop, final ChannelPromise promise)} 有对registed 这个状态的判断
 * 所以是不可行的
 * @author 86136
 */
public class ClientAndServer {
    public static NioServerSocketChannel channel ;
    public static CountDownLatch countDownLatch =new CountDownLatch(1);
    
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(8);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).
                handler(new ChannelInboundHandlerAdapter(){
             //在channel 注册到eventLoop上时会传递channelActive 事件
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("channel已经注册到NioEventLoop");
                channel = (NioServerSocketChannel)ctx.channel();
                countDownLatch.countDown();
                super.channelActive(ctx);
            }
        });

        //作为客户端去连接 9098服务器
        new Thread(()->{
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            CountDownLatch curCount =new CountDownLatch(1);
            //开启channel 作为客户端连接9098
            //把channel再次注册到另一个eventLoop上
            bossGroup.register(channel).addListener(new ChannelFutureListener() {
                //channel注册成后调用这个
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        //不能直接直接注册，要另外启以个线程去注册，因为执行这个Listener是channel的io线程
//                        ChannelFuture connect = future.channel().pipeline().connect(new LocalAddress("9098"));
//                        connect.channel().closeFuture().sync(); // 关闭连接
                        new Thread(()->{
                            try {
                                ChannelFuture connect = future.channel().pipeline().connect(new LocalAddress("9098"));
                                connect.channel().closeFuture().sync(); // 关闭连接
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }

                }
            });
            try {
                curCount.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //睡眠20秒，作为客户端给9098发送消息
            String str="客户端消息";
            byte[] req = str.getBytes();
            ByteBuf buffer = Unpooled.buffer(req.length);
            buffer.writeBytes(req);
            channel.writeAndFlush(buffer);
        }).start();


        serverBootstrap.childHandler(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("1111112222");
                super.channelActive(ctx);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                try {

                    ByteBuf buf =(ByteBuf)msg;
                    System.out.println(buf.toString(CharsetUtil.UTF_8));
                    String backMsg="j122";
                    ByteBuf retBuf= Unpooled.buffer(backMsg.length());//这里不用创建的话会出现 引用计数为0的ByteBuf而引发的报错
                    retBuf.writeBytes(backMsg.getBytes());

//                    ctx.channel().write(retBuf);
//                    new Thread(()->{
//                        ctx.write(retBuf);
//                    }).start();
                    System.out.println((ctx.executor()==ctx.channel().eventLoop())+"#############w#######");
                    System.out.println(ctx.executor().inEventLoop()+"************");
                    System.out.println((ctx.channel().eventLoop().inEventLoop())+"%%%%%%%%%%%%%%w%%%%%");

                    boolean b = ctx.channel().eventLoop() == ctx.executor();
                    System.out.println(b+"#############");
                    System.out.println(ctx.channel().eventLoop().equals(ctx.executor())+"$$$$$$$$$$$w$$$$$$$$");
                    System.out.println( ctx.channel().eventLoop().inEventLoop(Thread.currentThread())+"&&&&&&w&&&&&&&&&&&");
                    ctx.writeAndFlush(retBuf).await(); // 回应的输出操作
                    ctx.close();
                } finally {
                    ReferenceCountUtil.release(msg) ; // 释放缓存
                }
            }

        });

        //作为服务端绑定9000端口
        //这里不能直接注册，要初始化channel
        ChannelFuture future = serverBootstrap.bind(9000).sync();
        future.channel().closeFuture().sync();// 等待Socket被关闭
    }
}
