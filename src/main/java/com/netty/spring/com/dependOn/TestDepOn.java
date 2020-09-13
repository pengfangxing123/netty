package com.netty.spring.com.dependOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * DependsOn 是不允许的，if (isDependent(beanName, dep)) {
 * 							throw new BeanCreationException(mbd.getResourceDescription(), beanName,
 * 									"Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
 *                                                }
 *
 * 构造方法是不允许循环引用的，创建的对象的时候用到不三级缓存
 *
 * @Autowired 是可以的，用三级缓存处里
 * @author 86136
 */
@Configuration
//@DependsOn("testDepOn1")
public class TestDepOn {
//    private TestDepOn1 testDepOn1;
//    public TestDepOn(TestDepOn1 testDepOn1) {
//        this.testDepOn1=testDepOn1;
//        System.out.println("TestDepOn 创建");
//    }
    @Autowired
    private TestDepOn1 testDepOn1;

    public TestDepOn() {
        System.out.println("TestDepOn 创建");
    }
}
