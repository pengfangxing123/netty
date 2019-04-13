package com.netty.server.server;

import com.netty.server.handler.HeartBeatHandler;
import com.netty.server.handler.HelloServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class HelloServer {
    private String host;
    private int port;

    public HelloServer(String host,int port){
        this.host=host;
        this.port=port;
    }

    public void start() {
        // 线程池是提升服务器性能的重要技术手段，利用定长的线程池可以保证核心线程的有效数量
        // 在Netty之中线程池的实现分为两类：主线程池（接收客户端连接）、工作线程池（处理客户端连接）
        EventLoopGroup bossGroup = new NioEventLoopGroup(10); // 创建接收线程池
        EventLoopGroup workerGroup = new NioEventLoopGroup(20); // 创建工作线程池
        System.out.println("服务器启动成功，监听端口为：" +port);
        try {
            // 创建一个服务器端的程序类进行NIO启动，同时可以设置Channel
            ServerBootstrap serverBootstrap = new ServerBootstrap();   // 服务器端
            // 设置要使用的线程池以及当前的Channel类型
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
            // 接收到信息之后需要进行处理，于是定义子处理器
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                    socketChannel.pipeline().addLast(new HeartBeatHandler());
//                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(2048)) ;
//                    socketChannel.pipeline().addLast(new JSONDecoder()) ;
//                    socketChannel.pipeline().addLast(new LengthFieldPrepender(4)) ;
//                    socketChannel.pipeline().addLast(new JSONEncoder()) ;
                    socketChannel.pipeline().addLast(new HelloServerHandler()); // 追加了处理器
                }
            });
            // 可以直接利用常亮进行TCP协议的相关配置
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // ChannelFuture描述的时异步回调的处理操作
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();// 等待Socket被关闭
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully() ;
            bossGroup.shutdownGracefully() ;
        }
    }

    public static void main(String[] args) {
        HelloServer server = new HelloServer("127.0.0.1", 9098);
        server.start();
//        for(;;){
//            System.out.println(LocalDateTime.now());
//            break;
//        }
    }


}
