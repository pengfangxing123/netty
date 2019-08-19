package com.netty.dynamicProxy;

import com.netty.dynamicProxy.service.Subject;

/**
 * @author fangxing.peng
 */
public class ProxyMain {
    public static void main(String[] args) {
        // 保存生成的代理类的字节码文件
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Subject proxy = new JDKProxy(new SubjectImpl()).getProxy();
        proxy.doSomething();
    }
}
