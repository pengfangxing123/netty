package com.netty.spring.com.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听 {@link ApplicationEvent}及其子类的事件
 * @author Administrator
 */
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

    //当容器发布此event事件后，该方法触发
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("收到事件"+event);
    }
}
