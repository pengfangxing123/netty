package com.netty.spring.xml.beandefinition;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;


/**
 * @author 86136
 */
public class Simple {
    public static void main(String[] args) {
        //testParent();

        //testImport();

        //testQualifier();

        //testDecorateBean();

        testInnerBean();

        //testLookUp();
    }

    private static void testLookUp() {
        ClassPathResource resource = new ClassPathResource("spring/lookUp.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
        factory.getBean("display");
    }

    private static void testInnerBean() {
        ClassPathResource resource = new ClassPathResource("spring/innerBean.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
        factory.getBean("hongqi");
    }

    /**
     * 没搞懂具体用来干嘛的
     */
    private static void testDecorateBean() {
        ClassPathResource resource = new ClassPathResource("spring/decorateBean.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
    }

    private static void testParent() {
        ClassPathResource resource = new ClassPathResource("spring/parent.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
    }

    private static void testImport() {
        ClassPathResource resource = new ClassPathResource("spring/import.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
    }

    private static void testQualifier() {
        ClassPathResource resource = new ClassPathResource("spring/qualifier.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
    }
}
