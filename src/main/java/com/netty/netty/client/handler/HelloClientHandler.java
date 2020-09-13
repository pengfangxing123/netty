package com.netty.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class HelloClientHandler extends ChannelInboundHandlerAdapter {
    private String str = "In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book" +
            " will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process" +
            " of configuring and connecting all of Netty’s components to bring your learned about threading models in ge" +
            "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss" +
            "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi" +
            "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the" +
            "process of configuring and connecting all of Netty’s components to bring your learned about threading " +
            "models in general and Netty’s threading model in particular, whose performance and consistency advantag" +
            "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri" +
            "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222" +
            "sdsa ddasd asdsadas dsadasdas"+System.getProperty("line.separator");

    private AtomicInteger count=new AtomicInteger(0);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(IdleStateEvent.class.isAssignableFrom(evt.getClass())){
            IdleStateEvent event=(IdleStateEvent)evt;
            if(event.state()== IdleState.READER_IDLE &&count.intValue()<4){
                count.getAndAdd(1);
                String re_mess="第"+count.intValue()+"发送";
                byte[] bytes = re_mess.getBytes();
                ByteBuf buffer = Unpooled.buffer(bytes.length);
                buffer.writeBytes(bytes);
                ctx.channel().writeAndFlush(buffer);

            }else{
                ctx.fireUserEventTriggered(evt);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        str="客户端消息";
        byte[] req = str.getBytes();
        ByteBuf buffer = null;
//        ByteBuf duplicate = buffer.duplicate();//复制当前对象，复制后的对象与前对象共享缓冲区，且维护自己的独立索引
//        ByteBuf copy = buffer.copy();//复制一份全新的对象，内容和缓冲区都不是共享的
//        buffer.slice();//获取调用者的子缓冲区，且与原缓冲区共享缓冲区

//        for (int i=0;i<2;i++){
//            buffer = Unpooled.buffer(req.length);
//            buffer.writeBytes(req);
//            ctx.writeAndFlush(buffer).addListener(new ChannelFutureListener() {
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    System.out.println(future.channel().isWritable());
//                    System.out.println("发送成功");
//                }
//            });
//        }
        boolean writable = ctx.channel().isWritable();
        buffer = Unpooled.buffer(req.length);
        buffer.writeBytes(req);
        System.out.println((ctx.executor()==ctx.channel().eventLoop())+"####################");
        System.out.println(ctx.executor().inEventLoop()+"************");
        ctx.writeAndFlush(buffer).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println(future.channel().isWritable());
                System.out.println("发送成功");
            }
        });
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接被关闭");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("连接关闭后 睡眠完毕");
        super.channelInactive(ctx);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 只要服务器端发送完成信息之后，都会执行此方法进行内容的输出操作
        try {
            ByteBuf buf =(ByteBuf)msg;
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        } finally {
            ReferenceCountUtil.release(msg); // 释放缓存
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("全部结束");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public static void main(String[] args) {
        int i;
        int j=1;
       switch (j){
           case 1 :
           case 2 :  i=2;
           case 3:i=3;
           break;
           default:i=4;
       }
        System.out.println(i);
    }
}
