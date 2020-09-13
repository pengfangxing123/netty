package com.netty.io.nio.reactor;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 单个channle注册到多个seleoct上
 * 语法上是允许的
 * @author 86136
 */
public class ScMsTest {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        //Inet4Address.getLocalHost()只能通过本机的ipv4访问
        socketChannel.socket().bind(new InetSocketAddress(Inet4Address.getLocalHost(),8099));
        //jdk大于1.7也可以这样绑定
        //socketChannel.bind(new InetSocketAddress(Inet4Address.getLocalHost(),8090));

        //开启多个个selector
        Selector selector = Selector.open();
        Selector selector2 = Selector.open();

        //将socket连接注册到selector
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        socketChannel.register(selector2, SelectionKey.OP_ACCEPT);
    }
}
