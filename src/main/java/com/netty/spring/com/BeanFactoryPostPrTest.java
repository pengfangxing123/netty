package com.netty.spring.com;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * BeanFactoryPostProcessor Bean定义加载完成，但是Bean还未实例化时执行
 * @author Administrator
 */
@Component
public class BeanFactoryPostPrTest implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("bean定义数量==>"+beanFactory.getBeanDefinitionCount());
        System.out.println("bean数量"+beanFactory.getSingletonCount());//这里会有一些，但是是spring自己的
    }
}
