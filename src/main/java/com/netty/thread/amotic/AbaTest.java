package com.netty.thread.amotic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author 86136
 */
public class AbaTest {
    private static AtomicInteger atomicInt = new AtomicInteger(100);
    private static AtomicStampedReference atomicStampedRef = new AtomicStampedReference(100, 0);

    private static AtomicReference<PerSon> reference=new AtomicReference();

    public static void main(String[] args) throws InterruptedException {
        //intAba();

        //引用类型的aba
        referenceAba();
    }

    /**
     * 这个更加清楚的可以看aba的问题，
     * 因为存的是value内存中的地址，及时a的内容变了，reference.compareAndSet(a, b)还是返回true
     * 如果是链表的话，next指向变了的话就会导致链表完全不一样了
     */
    private static void referenceAba() {
        AbaTest abaTest = new AbaTest();
        PerSon a = abaTest.new PerSon("1", "2");
        PerSon b = abaTest.new PerSon("2", "3");
        reference.set(a);
        Thread intT1 = new Thread(new Runnable() {
            @Override
            public void run() {
                reference.compareAndSet(a, b);
                reference.compareAndSet(b, a);
                a.setAge("20");
                a.setName("change name");
            }
        });

        Thread intT2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
                boolean c3 = reference.compareAndSet(a, b);//true
                System.out.println(c3);
            }
        });

        intT1.start();
        intT2.start();
    }


    private static void intAba() throws InterruptedException {
        Thread intT1 = new Thread(new Runnable() {
            @Override
            public void run() {
                atomicInt.compareAndSet(100, 101);
                atomicInt.compareAndSet(101, 100);
            }
        });

        Thread intT2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
                boolean c3 = atomicInt.compareAndSet(100, 101);
                System.out.println(c3); // true
            }
        });

        intT1.start();
        intT2.start();
        intT1.join();
        intT2.join();

        Thread refT1 = new Thread(new Runnable() {
            @Override
            public void run(){
              try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
              atomicStampedRef.compareAndSet(100, 101, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
              atomicStampedRef.compareAndSet(101, 100, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
        }
    });

        Thread refT2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int stamp = atomicStampedRef.getStamp();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                }
                boolean c3 = atomicStampedRef.compareAndSet(100, 101, stamp, stamp + 1);
                System.out.println(c3); // false
            }
        });

        refT1.start();
        refT2.start();
    }

    class PerSon{
        private String name;
        private String age;

        public PerSon(String name, String age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
