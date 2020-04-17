package com.netty.io.bio.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Administrator
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        System.out.println("start...");

        DatagramSocket ds = new DatagramSocket(2000);

        byte[] buf = new byte[512];
        DatagramPacket receive = new DatagramPacket(buf, buf.length);

        ds.receive(receive);

        byte[] data = receive.getData();
        System.out.println(new String(data,0,data.length,"UTF-8"));

        String responseData="接受到收据："+data.length;
        byte[] bytes = responseData.getBytes();

        final InetAddress address = receive.getAddress();
        int port = receive.getPort();

        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, port);
        ds.send(datagramPacket);

        System.out.println("end...");
        ds.close();
    }
}
