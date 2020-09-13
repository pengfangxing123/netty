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

/**
 * 服务端又是客户端
 * 将channel注册到一个eventLoop，然后注册bind，和connect
 * 这中方法同样不行，因为我们的channel是 NioServerSocketChannel 它的doConnect是抛出一个异常
 * @author 86136
 */
public class ClientAndServer2 {
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
                System.out.println("channel已经注册到NioEventLoo   p");
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
            new Thread(()->{
                try {
                    ChannelFuture connect = channel.pipeline().connect(new LocalAddress("9098"));
                    curCount.countDown();
                    connect.channel().closeFuture().sync(); // 关闭连接
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                curCount.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String str="9000 客户端消息";
            byte[] req = str.getBytes();
            ByteBuf buffer = Unpooled.buffer(req.length);
            buffer.writeBytes(req);
            channel.writeAndFlush(buffer);
        }).start();


        serverBootstrap.childHandler(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("11111123212222");
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
                    System.out.println((ctx.executor()==ctx.channel().eventLoop())+"##321###########w#######");
                    System.out.println(ctx.executor().inEventLoop()+"3354");
                    System.out.println((ctx.channel().eventLoop().inEventLoop())+"%%%%%%%%%%%45%%%w%%%%%");

                    boolean b = ctx.channel().eventLoop() == ctx.executor();
                    System.out.println(b+"###########55##");
                    System.out.println(ctx.channel().eventLoop().equals(ctx.executor())+"$$$66$$$$$$$$w$$$$$$$$");
                    System.out.println( ctx.channel().eventLoop().inEventLoop(Thread.currentThread())+"&&&&&&w77&&&&&&&&&&&");
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
