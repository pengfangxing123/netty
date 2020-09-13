package com.netty.rpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 服务发布handler
 * @author 86136
 */
public class RpcRemoteServiceNamespaceHandler  extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service", new ProviderBeanDefinitionParser());
        registerBeanDefinitionParser("application", new ApplicationConfigDefinitionParser());
    }
}
