package com.netty.thread.threadLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86136
 */
public class ThreadLocalDemo {
    private List<Val<Integer>> list =new ArrayList<>();

    ThreadLocal<Val<Integer>> c =new ThreadLocal<Val<Integer>>(){
        @Override
        protected Val<Integer> initialValue() {
            Val<Integer> val = new Val<>();
            val.data=0;
            synchronized (new Object()){
                list.add(val);
            }
            return val;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalDemo demo = new ThreadLocalDemo();
        for(int i=0;i<50;i++){
            new Thread(() -> {
                Val<Integer> val = demo.c.get();
                val.data++;
                demo.c.set(val);
            }).start();
        }

        Thread.sleep(3000);
        int sum = demo.list.stream().mapToInt(p -> p.data).sum();
        System.out.println(sum);
    }

}

class Val<T>{
     T data;

}
