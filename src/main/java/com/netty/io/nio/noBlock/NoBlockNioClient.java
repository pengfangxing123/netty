package com.netty.io.nio.noBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NoBlockNioClient {
     private SocketChannel clientSocketChannel;

     public NoBlockNioClient() throws IOException, InterruptedException {
         // 打开 Client Socket Channel
         clientSocketChannel = SocketChannel.open();
         // 配置为非阻塞,把这里注释掉，确保已经连接上
         //clientSocketChannel.configureBlocking(false);

         clientSocketChannel.connect(new InetSocketAddress(8090));

         ByteBuffer buffer = ByteBuffer.allocate(1024);
         String content="非阻塞Io";
         buffer.put(content.getBytes(StandardCharsets.UTF_8));
         buffer.flip();
         clientSocketChannel.write(buffer);
         System.out.println("发送成功"+content);

         buffer.clear();
         int read = clientSocketChannel.read(buffer);
         buffer.flip();
         byte[] bytes = new byte[read];
         buffer.get(bytes);
         System.out.println("接受成功"+new String(bytes,StandardCharsets.UTF_8));
         TimeUnit.SECONDS.sleep(5);
     }




    public static void main(String[] args) throws IOException, InterruptedException {
         NoBlockNioClient client = new NoBlockNioClient();
    }
 }