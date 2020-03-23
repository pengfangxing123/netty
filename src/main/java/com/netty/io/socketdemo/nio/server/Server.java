package com.netty.io.socketdemo.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administrator
 */
public class Server {
    private int port;
    private ExecutorService threadPool;

    public Server(int port, ExecutorService threadPool) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public boolean start(){
        try {
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(port));

            server.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
