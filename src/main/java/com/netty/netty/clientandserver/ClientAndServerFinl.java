package com.netty.netty.clientandserver;

import com.netty.netty.client.client.HelloClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * 服务端又是客户端
 * @author 86136
 */
public class ClientAndServerFinl {
    public static NioSocketChannel channel ;
    public static CountDownLatch countDownLatch =new CountDownLatch(1);
    
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(8);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class);

        //作为客户端去连接 9098服务器
        new Thread(()->{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup).channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInboundHandlerAdapter(){
                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    String str="9000 finl客户端消息";
                    byte[] req = str.getBytes();
                    ByteBuf buffer = Unpooled.buffer(req.length);
                    buffer.writeBytes(req);
                    ctx.channel().writeAndFlush(buffer);
                }});
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9098);
            try {
                channelFuture.sync();
                channel= (NioSocketChannel) channelFuture.channel();
                countDownLatch.countDown();
                channelFuture.channel().closeFuture().sync(); // 关闭连接
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        //模拟其他线程 通过客户端Channel发送消息
        new Thread(()->{
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i=0;
            while (i<10){
                String str="9000 finl客户端消息：" +i;
                byte[] req = str.getBytes();
                ByteBuf buffer = Unpooled.buffer(req.length);
                buffer.writeBytes(req);
                try {
                    channel.pipeline().writeAndFlush(buffer).sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }

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
                    ctx.writeAndFlush(retBuf).await(); // 回应的输出操作
                    ctx.close();
                } finally {
                    ReferenceCountUtil.release(msg) ; // 释放缓存
                }
            }

        });

        //作为服务端绑定9000端口
        ChannelFuture future = serverBootstrap.bind(9000).sync();
        future.channel().closeFuture().sync();// 等待Socket被关闭
    }
}
