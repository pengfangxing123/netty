package com.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author 80004148
 */
public class Server {

    public static void main(String[] args) throws IOException {
        //开启一个socket连接
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.socket().bind(new InetSocketAddress(8090));
        //开启一个selector
        Selector selector = Selector.open();
        //将socket连接注册到selector
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            int n = selector.select();
            if (n==0)
                continue;//目前个人觉得 这个判断多余的 selector.select()会阻塞
            //获取准备就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();
                //连接事件
                if(key.isAcceptable()){
                    SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
                    channel.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));
                }

                if (key.isReadable()){
                    SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = channel.read(buffer);
                    System.out.println("读取到"+read);
                }
                if (key.isWritable() && key.isValid()){
                    System.out.println("写入");
                }
                if (key.isConnectable()){
                    System.out.println("isConnectable = true");
                }
                iterator.remove();

            }
        }
    }
}
