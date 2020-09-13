package com.netty.rpc.simple.service.impl;

import com.netty.rpc.simple.service.HelloService;

/**
 * @author fangxing.peng
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "认真，坚持";
    }
}
