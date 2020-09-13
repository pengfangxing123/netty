package com.netty.spring.com.dependOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author 86136
 */
@Configuration
//@DependsOn("testDepOn")
public class TestDepOn1 {

//    private TestDepOn testDepOn;
//    public TestDepOn1(TestDepOn testDepOn) {
//        this.testDepOn=testDepOn;
//        System.out.println("TestDepOn1 创建");
//    }

    @Autowired
    private TestDepOn testDepOn;

    public TestDepOn1() {
        System.out.println("TestDepOn 创建");
    }
}
