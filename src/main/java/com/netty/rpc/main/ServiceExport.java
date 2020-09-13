package com.netty.rpc.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 86136
 */
public class ServiceExport {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("rpc/ares-server.xml");
        System.out.println("服务发布完成");
    }
}
