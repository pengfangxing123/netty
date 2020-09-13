package com.netty.reference;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

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

            //new Thread(this::printFf).start();
        }
        @Override
        public String toString() {
            return "[id=" + id + ", name=" + name + "]";
        }

        public void printFf(){
            for(int i=0;;i++){
                System.out.println("当前值"+i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize()执行");
            super.finalize();
        }
    }

    static class UserWeakReference extends WeakReference<User>{
        private int id;
        private String name;

        public UserWeakReference(User referent, ReferenceQueue queue) {
            super(referent, queue);
            this.id=referent.id;
            this.name=referent.name;
        }

        @Override
        public String toString() {
            return "UserWeakReference{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}'
                    +"referent=";
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("UserWeakReference finalize() start...");
            super.finalize();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //testSimple();
        //testQueue();
        //testQueue2();

        testQueue3();
        //testReachabilityFence();

        //testReachabilityFence2();
    }

    private static void testSimple() {
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
     * 测试ReferenceQueue 这个放入队列的是什么
     * 放的是reference被回收的弱引用对象
     * 这里是wr
     */
    private static void testQueue() {
        User u = new User(1,"geym");
        ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
        WeakReference<User> wr = new WeakReference<User>(u,refQueue);
        System.out.println("gc前get 获取引用对象"+wr.get());
        System.out.println("gc前从referenceQueue队列中获取引用对象"+refQueue.poll());
        //将强引用置空
        u=null;
        System.gc();
        System.out.println("After GC:");
        System.out.println("gc后get 获取引用对象"+wr.get());
        System.out.println("gc后从referenceQueue队列中获取引用对象"+refQueue.poll());
    }

    /**
     * 测试ReferenceQueue 这个放入队列的是什么
     * 放的是reference被回收的弱引用对象
     * 这里是wr
     */
    private static void testQueue2() {
        User u = new User(2,"geym2");
        ReferenceQueue<UserWeakReference> refQueue = new ReferenceQueue<>();
        UserWeakReference wr = new UserWeakReference(u,refQueue);
        System.out.println("queue2 gc前get 获取引用对象"+wr.get());
        System.out.println("queue2 gc前从referenceQueue队列中获取引用对象"+refQueue.poll());
        //将强引用置空
        u=null;
        System.gc();
        System.out.println("After GC:");
        System.out.println("queue2 gc后get 获取引用对象"+wr.get());
        Reference<? extends UserWeakReference> poll = refQueue.poll();
        System.out.println("queue2 gc后从referenceQueue队列中获取引用对象"+poll);
        //相等
        System.out.println("队列中取出的引用对象是否相等"+(poll.equals(wr)));
    }


    private static void testQueue3() {
        ReferenceQueue<UserWeakReference> refQueue = new ReferenceQueue<>();
        testQueue4(refQueue);
        System.gc();
        System.out.println("After GC:");
        System.gc();
        Reference<? extends UserWeakReference> poll = refQueue.poll();
        System.out.println("queue3 gc后从referenceQueue队列中获取引用对象"+poll);
    }

    private static void testQueue4(ReferenceQueue<UserWeakReference> refQueue) {
        User u = new User(2,"geym2");
        UserWeakReference wr = new UserWeakReference(u,refQueue);
        System.out.println("queue4 gc前get 获取引用对象"+wr.get());
        System.out.println("queue4 gc前从referenceQueue队列中获取引用对象"+refQueue.poll());
        //将强引用置空
        u=null;
        System.gc();
        System.out.println("After GC:");
        System.out.println("queue4 gc后get 获取引用对象"+wr.get());
    }

    /**
     * 测试ReferenceQueue 这个放入队列的是什么
     * 放的是reference被回收的弱引用对象
     * 这里是wr
     * 这里throws InterruptedException 的话会导致从队列中获取不到UserWeakReference，不直到什么原因
     */
    private static void testReachabilityFence() throws InterruptedException {
        User u = new User(3,"geym2");
        ReferenceQueue<UserWeakReference> refQueue = new ReferenceQueue<>();
        UserWeakReference wr = new UserWeakReference(u,refQueue);
        System.out.println("queue2 gc前get 获取引用对象"+wr.get());
        System.out.println("queue2 gc前从referenceQueue队列中获取引用对象"+refQueue.poll());

        //开启一个线程去调用类似Reference.reachabilityFence(this)的方法
        new Thread(()->{
            try {
                userReachabilityFence(wr,refQueue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        //jdk 9里面才有这个方法
        //Reference.reachabilityFence(this);
        //jdk 9以下用下面这个 加了这个表示对u有一个强引用；
        //为了防止 u对象还有方法在进行，而把它回收了(实验没成功- -)
        synchronized (u){

        }
        //将强引用置空
        u=null;
        TimeUnit.SECONDS.sleep(10);

    }

    /**
     * 尝试去正在允许 且没有直接引用的对象，
     * 实验失败- -
     */
    public static void  testReachabilityFence2(){
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(;;){
                System.gc();
            }
        }).start();

        new User(1,"555").printFf();
    }

    public static void userReachabilityFence(UserWeakReference wr,ReferenceQueue<UserWeakReference> refQueue) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        System.gc();
        System.out.println("After GC:");
        System.out.println("queue2 gc后get 获取引用对象"+wr.get());
        Reference<? extends UserWeakReference> poll = refQueue.poll();
        System.out.println("queue2 gc后从referenceQueue队列中获取引用对象"+poll);
    }
    /**
     * 方法调用后，内存会被回收
     * @param
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
