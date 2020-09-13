package com.netty.collect.list;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 86136
 */
public class ArrayListSimple {
    public static void main(String[] args) {
        testUnmodify();
    }

    /**
     * unmodify后，原集合是可以修改的，修改也还可以反馈到 unmodify集合中
     * 原理就是unmodify集合实际上就是对原集合做了个包装，调用add方法会抛出异常
     */
    private static void testUnmodify() {
        ArrayList<Object> objects = new ArrayList<>();

        objects.add(1);
        objects.add(2);
        List<Object> unmodifi = Collections.unmodifiableList(objects);
        System.out.println("unmodifi 集合元素："+unmodifi);
        //会抛出异常，runtime异常可以不用捕获或者抛出
        //unmodifi.add(4);
        System.out.println("unmodifi 集合添加元素后unmodifi集合元素："+unmodifi);

        objects.add(5);
        System.out.println("原集合添加元素后，unmodifi集合元素："+unmodifi);
    }
}
