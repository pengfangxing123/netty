package com.netty.socketDemo;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Administrator
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(2000);
        System.out.println("server start ...");

        server=ininServer(server);

        for(;;){
            Socket client = server.accept();
            ClientHandler clientHandler = new ClientHandler(client);
            clientHandler.start();
        }
    }

    private static ServerSocket ininServer(ServerSocket server) throws Exception {
        server.setReuseAddress(true);
        server.setReceiveBufferSize(64*1024*1024);
        server.setPerformancePreferences(1,1,1);
        return server;
    }


    static class ClientHandler extends Thread{
        private Socket socket;
        private boolean flag=true;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("client connect success ,info :" +socket.getInetAddress()
                    +":"+socket.getPort());
            PrintStream write=null;
            BufferedReader read=null;
            try {
                write = new PrintStream(socket.getOutputStream());

                read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                do {
                    String str = read.readLine();
                    if(str==null)
                        continue;
                    if ("bye".equalsIgnoreCase(str)){
                        flag=false;
                    }else{
                        System.out.println(str);
                        write.println(str.length());
                    }
                }while (flag);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    read.close();
                    socket.close();
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("clinet close");
        }
    }
}
