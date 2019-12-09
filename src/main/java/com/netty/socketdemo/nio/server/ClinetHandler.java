package com.netty.socketdemo.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author Administrator
 */
public class ClinetHandler {
    private SocketChannel socketChannel;



    class ClinetReadHandler extends Thread{
        private Selector readSelector;
        private ByteBuffer byteBuffer;

        public ClinetReadHandler(Selector readSelector) {
            this.readSelector = readSelector;
            this.byteBuffer = ByteBuffer.allocate(256);
        }

        @Override
        public void run() {
            do {
                try {
                    if(readSelector.select()==0){
                        continue;
                    }

                    Iterator<SelectionKey> iterator = readSelector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(!key.isValid()){
                            continue;
                        }
                        byteBuffer.clear();
                        SocketChannel channel = (SocketChannel) key.channel();
                        int read = channel.read(byteBuffer);
                        if(read>0){
                            //length-1是为了丢弃换行符
                            String str = new String(byteBuffer.array(), 0, byteBuffer.position() - 1);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
