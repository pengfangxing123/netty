package com.netty.rpc.simple;

import com.netty.rpc.simple.service.impl.HelloServiceImpl;

/**
 * @author fangxing.peng
 */
public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloServiceImpl helloService = new HelloServiceImpl();
        RpcFramework.export(helloService,8090);
        System.out.println("server is end...");
    }
}
