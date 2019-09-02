package com.netty.dynamicProxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Administrator
 */
public class CglibProxyTest {
    public static void main(String[] args) throws Exception {
        // 代理类class文件存入本地磁盘方便我们反编译查看源码D:\idea\netty\com\sun\proxy
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\idea\\netty");

        CglibTestSon CglibTestSon = new CglibTestSon();

        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibTestSon.class);

        Callback s = new MthdInvoker(CglibTestSon);
        Callback callbacks[] = new Callback[] { s };
        enhancer.setCallbacks(callbacks);
        CglibTestSon CglibTestSon2 = (CglibTestSon) enhancer.create();
        CglibTestSon2.gotoHome();
        CglibTestSon2.gotoSchool();
        ////这里可以看到这个类以及被代理，在执行方法前会执行aopMethod（）。这里需要注意的是oneDay（）方法和onedayFinal（）的区别。onedayFinal的方法aopMethod执行2次，oneDay的aopMethod执行1次 ,注意这里和jdk的代理的区别
        CglibTestSon2.oneday();
        CglibTestSon2.onedayFinal();
    }


    /**
     * 需要被代理的类，不需要实现顶层接口
     */
    static class CglibTestSon {
        public CglibTestSon() {
        }
        public void gotoHome() {
            System.out.println("============gotoHome============");
        }
        public void gotoSchool() {
            System.out.println("===========gotoSchool============");
        }
        public void oneday() {
            gotoHome();
            gotoSchool();
        }
        public final void onedayFinal() {
            gotoHome();
            gotoSchool();
        }
    }
    /**
     * 可以类比于jdk动态代理中的InvocationHandler ，实际上被代理后重要的类，实际上后续执行的就是intercept里的方法，
     * 如果需要执行原来的方法，则调用 method.invoke(s, args);这里也加了一个aopMethod();
     */
    static class MthdInvoker implements MethodInterceptor {
        private CglibTestSon s;

        public MthdInvoker(CglibTestSon s) {
            this.s = s;
        }

        private void aopMethod() {
            System.out.println("i am aopMethod");
        }

        /**
         * https://blog.csdn.net/yhl_jxy/article/details/80633194
         * @param obj 表示增强的对象，即实现这个接口类的一个对象；
         * @param method method表示要被拦截的方法；
         * @param args
         * @param proxy 表示要触发父类的方法对象；
         * @return
         * @throws Throwable
         */
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            aopMethod();
            Object a = method.invoke(s, args);
            return a;
        }
    }
}

