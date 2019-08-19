package com.netty.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author fangxing.peng
 */
public class JDKProxy implements InvocationHandler {
    private Object target;

    public JDKProxy(Object target) {
        this.target = target;
    }

    /**
     * 获取被代理接口实例对象
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        //1、为接口创建代理类的字节码文件
        //2、使用ClassLoader将字节码文件加载到JVM
        //3、创建代理类实例对象
        T t = (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        return t;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke before...");
        Object invoke = method.invoke(target, args);
        System.out.println("invoke after...");
        return invoke;
    }
}
