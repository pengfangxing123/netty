package com.netty.Lock;

import sun.applet.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Circle {
    private double radius = 0;
    public static int count =1;
    public Circle(double radius) {
        this.radius = radius;
    }

    class Draw {     //内部类
        public void drawSahpe() {
            System.out.println(radius);  //外部类的private成员
            System.out.println(count);   //外部类的静态成员
        }
    }

    public interface Love{
        String say(String str);
    }

    public void sayHello(){
        System.out.println("1111111111");
    }

    public static void main(String[] args) {
        Circle circle = new Circle(11);
        Draw draw = circle.new Draw();
        List list=new ArrayList<>();
        System.out.println("111111111");
        System.out.println(Thread.interrupted());
        Thread.currentThread().interrupt();
        System.out.println(Thread.currentThread().isInterrupted());
        System.out.println("222222222");


        List<String> strings = new ArrayList<>();
        strings.forEach(p->{

        });
        strings.stream().filter(p->p.equals("111")).collect(Collectors.toList());

        Love l=String::new;
        System.out.println(l.say("111"));
    }
}