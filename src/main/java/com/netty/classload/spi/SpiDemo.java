package com.netty.classload.spi;

import com.netty.classload.service.SPIService;

import java.sql.Driver;
import java.util.*;

/**
 * @author 86136
 */
public class SpiDemo {
    public static void main(String[] args) {
        testSimpleSpi();

        //testDriverContext();
    }

    /**
     * spi简单应用
     */
    private static void testSimpleSpi() {
        ServiceLoader<SPIService> loader = ServiceLoader.load(SPIService.class);

        Iterator<SPIService> iterator = loader.iterator();
        //真正创建对象是在hasNext 和next方法
        while (iterator.hasNext()){
            SPIService next = iterator.next();
            next.execute();
        }

        //测试迭代器创建后，再添加元素是否能被迭代出来，
        //答案是不可以的
//        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
//        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//        System.out.println("第一个迭代完");
//        map.put("first",120);
//        while (iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
    }

    /**
     * 测试上下文类加载器和spi在mysql驱动的应用
     */
    private static void testDriverContext() {
        /**
         当前类是由应用类加载器加载的，运行到这一行代码的时候，
         1. 因为java.util.ServiceLoader是在rt.jar中，所以通过双亲委托机制，会使用Bootstrap类加载器加载ServiceLoader。
         2. 但是我们要加载的是java.sql.Driver的实现类，一般都是在项目的lib包中。
         3. ServiceLoader的类加载器是Bootstrap类加载器，
            按照正常的模式会使用相同的类加载加载实现类(只能通过load.getClass().getClassLoader()获取类加载器)
         但是这个时候因为命名空间的原因，Bootstrap类加载器加载不到对应的实现类。
         4，所以就只能通过Thread获取上下文类加载器去加载
         */
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        //这里打印出的内容为null，也就是是Bootstrap类加载器
        System.out.println(load.getClass().getClassLoader());

        Iterator<Driver> iterator = load.iterator();
        while (iterator.hasNext()){
            Driver next = iterator.next();
            System.out.println(next + "  " + next.getClass().getClassLoader());
        }
    }

}

