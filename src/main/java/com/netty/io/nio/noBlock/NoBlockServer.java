package com.netty.io.nio.noBlock;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 非阻塞io，不是多路复用
 * 自旋会浪费cpu资源，基本不使用
 * @author 86136
 */
public class NoBlockServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        //Inet4Address.getLocalHost()只能通过本机的ipv4访问
        socketChannel.socket().bind(new InetSocketAddress(Inet4Address.getLocalHost(),8090));

        SocketChannel channel =null;
        boolean accept=true ,read =true ,write =true;
        while (accept){
            channel= socketChannel.accept();
            if(channel!=null){
                accept=false;
            }
        }


        while (read){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //这里会阻塞，也可以设置，默认是非阻塞的，
            //这个channel,是客户端的，是不是跟随那的配置 不太清楚
            //channel.configureBlocking(false);
            int readBytes = channel.read(buffer);
            buffer.flip();
            if(readBytes!=-1){
                System.out.println(readBytes);
                byte[] bytes = new byte[readBytes];
                buffer.get(bytes);
                System.out.println(new String(bytes,StandardCharsets.UTF_8));
                read=false;
            }
        }

        while (write){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String content="非阻塞Io";
            buffer.put(content.getBytes(StandardCharsets.UTF_8));
            channel.write(buffer);
        }
    }
}
