package com.netty.io.socketdemo.nio.server;

import java.nio.channels.*;
import java.util.Iterator;

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

