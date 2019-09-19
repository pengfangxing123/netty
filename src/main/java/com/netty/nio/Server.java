package com.netty.nio;

import java.io.IOException;
import java.net.Inet4Address;
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
        socketChannel.socket().bind(new InetSocketAddress(Inet4Address.getLocalHost(),8090));
        //开启一个selector
        Selector selector = Selector.open();

        //将socket连接注册到selector
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            int n = selector.select();
            if (n==0)
                continue;//目前个人觉得 这个判断多余的 selector.select()会阻塞
                        //补充：其实是有必要的，因为select()方法不会一直阻塞，因为epoll在timeout时间内如果没有事件，也会返回
            //获取准备就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();

                //忽略无效的 SelectionKey
                if(!key.isValid())
                    continue;
                //连接事件
                if(key.isAcceptable()){
                    SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
                    channel.configureBlocking(false);
                    //现在把客户端的channel也注册到了服务端的selector了
                    channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = channel.read(buffer);
                    if(read==-1){
                        System.out.println("没有读取到数据，连接已经被断开");
                        channel.register(selector,0);//参数为0取消该channel注册
                        return;
                    }
                    System.out.println("读取到"+read);
                }
                if (key.isWritable() && key.isValid()){
                    SocketChannel channel = (SocketChannel) key.channel();
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


