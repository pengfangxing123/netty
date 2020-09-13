package com.netty.rpc.simple;

import com.netty.rpc.simple.service.HelloService;

/**
 * @author fangxing.peng
 */
public class RpcConsumer {
    public static void main(String[] args) throws Exception {
        HelloService refer = RpcFramework.refer(HelloService.class, "127.0.0.1", 8090);
        System.out.println(refer.hello("hello..."));
        System.out.println(refer.hello("hello..."));
    }
}
