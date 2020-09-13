package com.dubbo;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.junit.Test;

/**
 * @author 86136
 */
public class ServiceBeanTest {
    private com.alibaba.dubbo.config.ApplicationConfig applicationConfig = new com.alibaba.dubbo.config.ApplicationConfig("first-dubbo-test");
    private com.alibaba.dubbo.config.RegistryConfig registryConfig = new com.alibaba.dubbo.config.RegistryConfig("zookeeper://120.79.201.200:2181");


    @Test
    public void testConfig() {
        com.alibaba.dubbo.config.ServiceConfig<DemoService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setInterface(DemoService.class);
        service.setRef(new DemoServiceImpl());
        service.export();

        com.alibaba.dubbo.config.ReferenceConfig<DemoService> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        reference.setInterface(DemoService.class);
        reference.setScope("remote");
        DemoService demoService = reference.get();
        DemoService demoService2 = reference.get();
        System.out.println(demoService.sayHello("dubbo"));
        System.out.println(demoService.todosomething("dubbo"));
    }
}
