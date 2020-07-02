package com.netty.spring.xml.service.imp;

import com.netty.spring.xml.service.Car;

/**
 * @author 86136
 */
public abstract class Display {
    public void display(){
        getCar().display();
    }

    public abstract Car getCar();
}
