package com.netty.rpc.netty;

import com.netty.rpc.common.entity.AresRequest;
import com.netty.rpc.serializer.common.SerializeType;
import com.netty.rpc.utils.PropertyConfigeHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 86136
 */
public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final static int boosGroupNum=1;

    private final static int workGroupNum=8;

    /**
     * 记录以及生成过的端口号，避免重复生成
     */
    private final Set<String> serverMap =Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * 单例
     */
    private static NettyServer nettyServer = new NettyServer();

    /**
     * 服务端boss线程组
     */
    private EventLoopGroup bossGroup;
    /**
     * 服务端worker线程组
     */
    private EventLoopGroup workerGroup;

    /**
     * 序列化类型配置信息
     */
    private SerializeType serializeType = PropertyConfigeHelper.getSerializeType();

    private Channel channel;

    public static NettyServer singleton(){
        return nettyServer;
    }

    public void start(final String port){
        if(!serverMap.add(port)){
            logger.info("[NettyServer]-[server start...]---端口：{}已经开启过server",port);
        }
        logger.info("[NettyServer]-[server start...]---端口：{}开始开启server",port);


        try {
            bossGroup = new NioEventLoopGroup(boosGroupNum);
            workerGroup = new NioEventLoopGroup(workGroupNum);
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //注册解码器NettyDecoderHandler
                            ch.pipeline().addLast(new NettyDecoderHandler(AresRequest.class, serializeType));
                            //注册编码器NettyEncoderHandler
                            ch.pipeline().addLast(new NettyEncoderHandler(serializeType));
                            //注册服务端业务逻辑处理器NettyServerInvokeHandler
                            ch.pipeline().addLast(new NettyServerInvokeHandler());
                        }
                    });
            try {
                channel = serverBootstrap.bind(Integer.parseInt(port)).sync().channel();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            logger.error("[NettyServer]-[server start...]---端口"+port+"开启失败",e);
            serverMap.remove(port);
        }


    }

    /**
     * 停止Netty服务
     */
    public void stop() {
        if (null == channel) {
            throw new RuntimeException("Netty Server Stoped");
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }

}
