package com.netty.spring.com;

import com.netty.spring.com.aspect.Car;
import com.netty.spring.com.config.MainConfig;
import com.netty.spring.com.service.UserService;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administrator
 */
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        Car car = (Car) applicationContext.getBean("car");
        car.run();

//        Object internalConfigurationAnnotationProcessor = applicationContext.getBean("org.springframework.context.annotation.internalConfigurationAnnotationProcessor");
//        System.out.println( internalConfigurationAnnotationProcessor.getClass().getTypeName());
//        UserService bean = applicationContext.getBean(UserService.class);
//        bean.insert();
//        //发布一个事件
//        applicationContext.publishEvent(new ApplicationEvent("hahahaha") {
//            @Override
//            public Object getSource() {
//                return super.getSource();
//            }
//
//            @Override
//            public String toString() {
//                return super.toString();
//            }
//        });
    }
}
