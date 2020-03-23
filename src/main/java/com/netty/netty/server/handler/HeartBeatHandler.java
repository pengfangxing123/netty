package com.netty.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger count=new AtomicInteger(0);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateEvent.class.isAssignableFrom(evt.getClass())
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event= (IdleStateEvent) evt;
            if(event.state()==IdleState.READER_IDLE){
                count.getAndAdd(1);
                if(count.intValue()>1){
                    System.out.println("超时2次，停止连接");
                    ctx.channel().close();
                }else{
                    System.out.println("超时1次");
                    ctx.fireUserEventTriggered(evt);
                }
            }
            ctx.fireUserEventTriggered(evt);
        }
    }

}
