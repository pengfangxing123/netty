package com.netty.socketDemo.nio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Administrator
 */
public class ClientListener extends Thread{
    private Selector selector;

    public ClientListener(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            do {
                int select = selector.select();
                if(select==0){
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(!key.isValid()){
                        continue;
                    }
                    handleKey(key);
                }
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleKey(SelectionKey key) throws Exception {
        if (key.isAcceptable()) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);


        }
    }
}

