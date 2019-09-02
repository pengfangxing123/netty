package com.netty.dynamicProxy;

import java.lang.reflect.Constructor;

/**
 * @author Administrator
 */
public class ReflectionTest {
    public static void main(String[] args) throws Exception{
//        Class<?> aClass = Class.forName("com.netty.dynamicProxy.Test2");
//        Constructor<?> constructor = aClass.getConstructor(String.class);
//        Test2 o = (Test2) constructor.newInstance("666");
//        o.getStr();
//        new Test1().getStr();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> aClass = classLoader.loadClass("com.netty.dynamicProxy.Test2");

    }

    class Test2 {
        private String str;
        public Test2(String str) {
            this.str=str;
        }

        public void getStr(){
            System.out.println(str);
        }
    }

    class Test1 {

        public void getStr() throws Exception {
            Class<?> aClass = Class.forName("com.netty.dynamicProxy.Test2");
            Constructor<?> constructor = aClass.getConstructor(String.class);
            Test2 o = (Test2) constructor.newInstance("666");
            o.getStr();
        }
    }
}
