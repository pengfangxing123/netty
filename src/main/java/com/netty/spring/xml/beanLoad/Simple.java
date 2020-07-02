package com.netty.spring.xml.beanLoad;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * @author 86136
 */
public class Simple {
    public static void main(String[] args) {
        testFactoryMethod();
    }

    private static void testFactoryMethod() {
        ClassPathResource resource = new ClassPathResource("spring/factoryMethod.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
        factory.getBean("car4");
    }
}
