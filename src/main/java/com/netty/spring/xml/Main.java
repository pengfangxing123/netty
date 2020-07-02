package com.netty.spring.xml;

import com.netty.spring.xml.service.imp.Display;
import com.netty.spring.xml.service.imp.Hongqi;
import com.netty.spring.xml.service.imp.HongqiSub;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 86136
 */
public class Main {
    public static void main(String[] args) {
        //testLookUp();

        testParent();

    }

    /**
     * 测试parent属性的用法
     *
     */
    private static void testParent() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/parent.xml");
        HongqiSub hongqiSub = (HongqiSub) context.getBean("hongqiSub");
        hongqiSub.printName();
        //Error creating bean with name 'hongqi': Bean definition is abstract
        Hongqi hongqi = (Hongqi) context.getBean("hongqi");
        System.out.println(hongqi.getName());
    }

    private static void testLookUp() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/lookUp.xml");
        Display display = (Display) context.getBean("display");
        display.display();
    }
}
