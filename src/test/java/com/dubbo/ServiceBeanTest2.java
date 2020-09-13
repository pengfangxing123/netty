package com.dubbo;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 86136
 */
public class ServiceBeanTest2 {
    private com.alibaba.dubbo.config.ApplicationConfig applicationConfig = new com.alibaba.dubbo.config.ApplicationConfig("first-dubbo-test");
    private com.alibaba.dubbo.config.RegistryConfig registryConfig = new com.alibaba.dubbo.config.RegistryConfig("zookeeper://120.79.201.200:2181");
    private com.alibaba.dubbo.config.ProtocolConfig protocolConfig = new com.alibaba.dubbo.config.ProtocolConfig("dubbo",20882);

    @Test
    public void testConfig() throws InterruptedException {
        ServiceConfig<DemoService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setInterface(DemoService.class);
        service.setRef(new DemoServiceImpl());
        service.export();

//        ServiceConfig<DemoService> service2 = new ServiceConfig<>();
//        service2.setApplication(applicationConfig);
//        service2.setRegistry(registryConfig);
//        service2.setProtocol(protocolConfig);
//        service2.setInterface(DemoService.class);
//        service2.setRef(new DemoServiceImpl());
//        service2.export();
        ServiceConfig<DemoService1> service2 = new ServiceConfig<>();
        service2.setApplication(applicationConfig);
        service2.setRegistry(registryConfig);
        service2.setProtocol(protocolConfig);
        service2.setInterface(DemoService1.class);
        service2.setRef(new DemoServiceImpl1());
        service2.export();
        TimeUnit.SECONDS.sleep(2000);
    }
}
