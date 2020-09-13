package com.dubbo;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 86136
 */
public class ServiceBeanTest3 {
    private com.alibaba.dubbo.config.ApplicationConfig applicationConfig = new com.alibaba.dubbo.config.ApplicationConfig("first-dubbo-test");
    private com.alibaba.dubbo.config.RegistryConfig registryConfig = new com.alibaba.dubbo.config.RegistryConfig("zookeeper://120.79.201.200:2181");
    private com.alibaba.dubbo.config.RegistryConfig registryConfig2 = new com.alibaba.dubbo.config.RegistryConfig("zookeeper://120.79.201.200:2182");

    @Test
    public void testConfig() throws InterruptedException {
        ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        reference.setInterface(DemoService.class);
        reference.setScope("remote");
        DemoService demoService = reference.get();
        System.out.println(demoService.sayHello("dubbo"));

        ReferenceConfig<DemoService> reference2 = new ReferenceConfig<>();
        reference2.setApplication(applicationConfig);
        //用一个错误的注册中心，模拟注册中心宕机
        reference2.setRegistry(registryConfig);
        reference2.setInterface(DemoService.class);
        reference2.setScope("remote");
        DemoService demoService2 = reference2.get();
        System.out.println(demoService2.sayHello("dubbo"));

        TimeUnit.SECONDS.sleep(2000);
    }
}
