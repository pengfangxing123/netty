package com.netty.spring.com.aspect;

import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author Administrator
 */
@Component
public class Car {
//    public Car(DataSource dataSource, ApplicationContext applicationContext){
//        System.out.println("car 被实例化");
//    }

    public void run(){
        System.out.println("car is runing now");
    }

    /**
     *  @PostConstruct 注解标记的方法是在 {@link InitDestroyAnnotationBeanPostProcessor}中
     *  一个BeanPostProcecos 去调用的
     */
    @PostConstruct
    public void postTest(){
        System.out.println("@PostConstruct");
    }
}
