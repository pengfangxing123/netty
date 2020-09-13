package com.netty.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;


/**
 *
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("1111112222");
        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
//            System.out.println(msg.getClass() + " **************");
//            Member member = (Member) msg ;
//            System.err.println("{服务器}" + member);
//            member.setName("【ECHO】" + member.getName());
            if(!ByteBuf.class.isAssignableFrom(msg.getClass())){
                //这个handler也注册到了 Server上，而客户端的连接事件
                ctx.fireChannelRead(msg);
                return;
            }
            ByteBuf buf =(ByteBuf)msg;
            System.out.println(buf.toString(CharsetUtil.UTF_8));
            //String backMsg="j1";
            //ByteBuf retBuf=Unpooled.buffer(backMsg.length());//这里不用创建的话会出现 引用计数为0的ByteBuf而引发的报错
            //retBuf.writeBytes(backMsg.getBytes());
           /* buf.clear();
            buf.writeBytes(backMsg.getBytes());*/
//            ChannelFuture channelFuture = ctx.writeAndFlush(retBuf);// 回应的输出操作
//            channelFuture.await();
//            ctx.channel().write(retBuf);
//            new Thread(()->{
//                ctx.write(retBuf);
//            }).start();
//            ctx.executor().execute(()->ctx.write(retBuf));
 //           System.out.println((ctx.executor()==ctx.channel().eventLoop())+"####################");
//            System.out.println(ctx.executor().inEventLoop()+"************");
//            System.out.println((ctx.channel().eventLoop().inEventLoop())+"%%%%%%%%%%%%%%%%%%%");
//
//            boolean b = ctx.channel().eventLoop() == ctx.executor();
//            System.out.println(b+"#############");
//            System.out.println(ctx.channel().eventLoop().equals(ctx.executor())+"$$$$$$$$$$$$$$$$$$$$");
//            System.out.println(ctx.channel().eventLoop().inEventLoop());
//            System.out.println(ctx.executor().inEventLoop());
//            System.out.println( ctx.channel().eventLoop().inEventLoop(Thread.currentThread())+"&&&&&&&&&&&&&&&&&");
            //ChannelFuture future = ctx.writeAndFlush(retBuf).await();// 回应的输出操作 这里如果给这个handler手动添加了exector，会死锁
//            ChannelFuture future = ctx.writeAndFlush(retBuf).addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    System.out.println(future.get());
//                }
//            });
//            new Thread(()->{
//                future.cancel(false);
//            }).start();
//            ctx.close();
        } finally {
            ReferenceCountUtil.release(msg) ; // 释放缓存
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("1111111111111111111111111");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
