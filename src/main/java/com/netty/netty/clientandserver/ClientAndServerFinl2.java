package com.netty.netty.clientandserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * 服务端又是客户端
 * @author 86136
 */
public class ClientAndServerFinl2 {
    public static NioSocketChannel channel ;
    public static CountDownLatch countDownLatch =new CountDownLatch(1);
    
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(8);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class);

        serverBootstrap.childHandler(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("111111232512222");
                super.channelActive(ctx);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                try {

                    ByteBuf buf =(ByteBuf)msg;
                    System.out.println(buf.toString(CharsetUtil.UTF_8));
                    String backMsg="j133252";
                    ByteBuf retBuf= Unpooled.buffer(backMsg.length());//这里不用创建的话会出现 引用计数为0的ByteBuf而引发的报错
                    retBuf.writeBytes(backMsg.getBytes());
                    ctx.writeAndFlush(retBuf).await(); // 回应的输出操作
                    //ctx.close();
                } finally {
                    ReferenceCountUtil.release(msg) ; // 释放缓存
                }
            }

        });

        //作为服务端绑定9000端口
        ChannelFuture future = serverBootstrap.bind(9000).await().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //这里这样搞还最好new Thread去处理，因为执行这里上一个的io线程
                if (future.isSuccess()){
                    NioSocketChannel socketChannel = new NioSocketChannel();
                    ChannelFuture registerFuture = bossGroup.next().register(socketChannel);
                    socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            System.out.println("作为客户端 channelActive");
                            int i=0;
                            while (i<10){
                                String str="9000 finl客户端消息：" +i;
                                byte[] req = str.getBytes();
                                ByteBuf buffer = Unpooled.buffer(req.length);
                                buffer.writeBytes(req);
                                ctx.writeAndFlush(buffer);
                                i++;
                            }
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            try {

                                ByteBuf buf =(ByteBuf)msg;
                                System.out.println(buf.toString(CharsetUtil.UTF_8));
                                String backMsg="j12332";
                                ByteBuf retBuf= Unpooled.buffer(backMsg.length());//这里不用创建的话会出现 引用计数为0的ByteBuf而引发的报错
                                retBuf.writeBytes(backMsg.getBytes());
                                ctx.writeAndFlush(retBuf); // 回应的输出操作
                                ctx.close();
                            } finally {
                                ReferenceCountUtil.release(msg) ; // 释放缓存
                            }
                        }

                    });
                    registerFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            socketChannel.connect(new InetSocketAddress("localhost", 9098));
                        }
                    });

                }
            }
        });
        future.channel().closeFuture().sync();// 等待Socket被关闭
    }
}
