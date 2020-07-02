package com.netty.spring.xml.customxmltag;

import com.netty.spring.xml.customxmltag.entity.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @author 86136
 */
public class Simple {
    public static void main(String[] args){
        //testSimple();
        testLoadBeanDefine();

    }

    private static void testLoadBeanDefine() {
        ClassPathResource resource = new ClassPathResource("spring/customXmlTag.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);
    }

    private static void testSimple() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/customXmlTag.xml");
        User user = (User) context.getBean("user");
        System.out.println(user.getUserName() + "----" + user.getEmail());
    }
}
