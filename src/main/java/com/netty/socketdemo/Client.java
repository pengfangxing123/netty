package com.netty.socketdemo;

import java.io.*;
import java.net.*;

/**
 * @author Administrator
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        socket.bind(new InetSocketAddress(Inet4Address.getLocalHost(),2001));

        socket=initSocket(socket);

        //连接超时3000ms
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(),2000),3000);
        System.out.println("连接成功...start");
        System.out.println("Client info"+socket.getLocalAddress()+":"+socket.getLocalPort());
        System.out.println("Server info"+socket.getInetAddress()+":"+socket.getPort());

        try {
            todo(socket);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fali,end");
        }finally {
            socket.close();
            System.out.println("client out");
        }

    }

    private static Socket initSocket(Socket socket) throws Exception {
        //设置读取超时时间
        socket.setSoTimeout(3000);
        //是否复用未完全关闭的socket地址（socket连接后，会占用本地的端口，socket连接
        // 关闭后，得等两分钟后才能使用该端口，设置为true后立即就能使用）
        socket.setReuseAddress(true);
        //是否开启Nagle算法（是否粘包发送）
        socket.setTcpNoDelay(false);
        //是否需要长时间无数据响应时发送确认数据（类似于心跳包），事件大约为2小时
        socket.setKeepAlive(true);
        //关闭后，20毫秒会直接丢弃缓冲区
        socket.setSoLinger(true,20);
        //设置接受，发送缓存区大小64K（小于64会直接发送，大于65会拆分），默认为32
        socket.setReceiveBufferSize(64*1024*1024);
        socket.setSendBufferSize(64*1024*1024);
        //权重
        socket.setPerformancePreferences(1 ,1,1);
        return socket;
    }

    private static void todo(Socket client) throws Exception {
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        //得到socket输出流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream print = new PrintStream(outputStream);

        //得到socket输入流
        InputStream inputStream = client.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            boolean flag=true;
            do {
                //读取键盘输入
                String str = input.readLine();
                //发送到服务器
                print.println(str);
                String echo = reader.readLine();
                System.out.println(echo);
                if("bye".equalsIgnoreCase(echo)){
                    flag=false;
                }
            } while (flag);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            print.close();
            reader.close();
        }
    }
}
