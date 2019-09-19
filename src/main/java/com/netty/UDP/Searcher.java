package com.netty.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Administrator
 */
public class Searcher {
    public static void main(String[] args) throws Exception {
        System.out.println("start...");

        DatagramSocket ds = new DatagramSocket();

        String responseData="Java 17k";
        byte[] bytes = responseData.getBytes();


        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        datagramPacket.setAddress(InetAddress.getLocalHost());
        datagramPacket.setPort(2000);
        ds.send(datagramPacket);


        byte[] buf = new byte[512];
        DatagramPacket receive = new DatagramPacket(buf, buf.length);

        ds.receive(receive);

        byte[] data = receive.getData();
        System.out.println(new String(data,0,data.length,"UTF-8"));

        System.out.println("end...");
        ds.close();
    }
}
