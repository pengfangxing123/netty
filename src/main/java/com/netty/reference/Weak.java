package com.netty.reference;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * 当你想引用一个对象，但是这个对象有自己的生命周期，你不想介入这个对象的生命周期，这时候你就是用弱引用
 * @author 86136
 */
public class Weak {
    public static class User{
        public int id;
        public String name;
        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
        @Override
        public String toString() {
            return "[id=" + id + ", name=" + name + "]";
        }

    }

    public static void main(String[] args) {
        User u = new User(1,"geym");
        //创建软引用对象
        WeakReference<User> wr = new WeakReference<User>(u);
        boolean enqueue = wr.enqueue();

        //奇怪1
        System.out.println(wr.get());
        //将强引用置空
        u=null;
        
        System.gc();
        System.out.println("After GC:");
        boolean enqueue2 = wr.enqueue();

        //这里很奇怪，有时候回收了。有时候没回收，估估计是System.out.println(wr.get()) ,又产生啥引用了User对象，有时候没来得及去掉引用
        System.out.println(wr.get());
        //原对象并没有被回收，所以WeakReference也是一个对象
        //上面这句话应该时错的，只有原对象被回收了，弱引用才会get不到值，但是WeakReference也是一个对象是正确的
        //这里也强引用了u这个对象
        System.out.println(u);
    }

    /**
     * 方法调用后，内存会被回收
     * @param args
     */
//    public static void main(String[] args) {
//        WeakReference<User> weak = getWeak();
//        System.out.println(weak.get());
//        System.gc();
//        System.out.println("After GC:");
//        System.out.println(weak.get());
//        //System.out.println( Runtime.getRuntime().availableProcessors());
//    }

    public static WeakReference<User> getWeak(){
        User b = new User(2, "static");
        WeakReference<User> wr = new WeakReference<User>(b);
        return wr;
    }
}
