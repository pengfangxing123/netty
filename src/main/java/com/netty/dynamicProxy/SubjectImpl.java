package com.netty.dynamicProxy;

import com.netty.dynamicProxy.service.Subject;

/**
 * @author fangxing.peng
 */
public class SubjectImpl implements Subject {

    @Override
    public void doSomething() {
        System.out.println("success..");
    }
}
