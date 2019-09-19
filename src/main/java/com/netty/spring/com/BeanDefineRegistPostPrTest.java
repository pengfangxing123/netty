package com.netty.spring.com;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 是BeanFactoryPostProcessor的子类
 * 执行时机：在所有bean定义信息将要被加载，bean实例还未创建的；优先于BeanFactoryPostProcessor执行；
 * 作用：利用BeanDefinitionRegistryPostProcessor给容器中再额外添加一些组件，在标准初始化之后修改applicationContext的内部bean registry
 * 就是原本的已经加完了，但是还没最终确定，到了BeanFactoryPostProcessor就是最终确定了
 */
@Component
public class BeanDefineRegistPostPrTest implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("bean定义数量==>"+registry.getBeanDefinitionCount());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("bean定义数量==>"+beanFactory.getBeanDefinitionCount());
    }
}
