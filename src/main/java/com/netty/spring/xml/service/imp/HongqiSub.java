package com.netty.spring.xml.service.imp;

import com.netty.spring.xml.service.Car;

/**
 * @author 86136
 */
public class HongqiSub{
    private String name;

    //用来测试qualifier
    private Hongqi tt;

    public void printName() {
        System.out.println(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
