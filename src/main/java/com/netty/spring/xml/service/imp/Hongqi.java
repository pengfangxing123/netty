package com.netty.spring.xml.service.imp;

import com.netty.spring.xml.service.Car;

/**
 * @author 86136
 */
public class Hongqi implements Car {
    private String name;

    private InnerCar innerCar;

    @Override
    public void display() {
        System.out.println("我是 hongqi");
    }

    public void parent(){
        System.out.println("父类方法");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InnerCar getInnerCar() {
        return innerCar;
    }

    public void setCar(InnerCar innerCar) {
        this.innerCar = innerCar;
    }
}
