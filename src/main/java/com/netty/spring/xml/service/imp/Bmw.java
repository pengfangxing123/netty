package com.netty.spring.xml.service.imp;

import com.netty.spring.xml.service.Car;

/**
 * @author 86136
 */
public class Bmw implements Car {
    @Override
    public void display() {
        System.out.println("我是 BMW");
    }
}
