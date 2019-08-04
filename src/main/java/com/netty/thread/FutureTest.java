package com.netty.thread;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 */
public class FutureTest {

        public static void main(String[] args) throws ExecutionException, InterruptedException {
                Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

//            Callable callable =new Callable<Integer>() {
//                @Override
//                public Integer call() throws Exception {
//                    Thread.sleep(5000);
//                    System.out.println("2222222211");
//                    return 1111;
//                }
//            };
//
//            FutureTask futureTask = new FutureTask<>(callable);
//            Thread thread = new Thread(futureTask);
//            thread.start();
//
//            System.out.println("1111111111111");
//            System.out.println(futureTask.get());

//                int i=5;
//                do {
//                    System.out.println(i);
//                    --i;
//                }while (i>0);
//                if (i<0){
//                    System.out.println("#####################");
//                }
//        AtomicInteger atomicInteger = new AtomicInteger(Integer.MIN_VALUE);
//        int i=atomicInteger.incrementAndGet();
//        for(i=1;i<10;i++){
//                System.out.println(i);
//        }

        }
}
