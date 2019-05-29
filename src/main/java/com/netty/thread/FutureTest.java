package com.netty.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * @author Administrator
 */
public class FutureTest {


        public static void main(String[] args) throws ExecutionException, InterruptedException {
            Callable callable =new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(5000);
                    System.out.println("2222222211");
                    return 1111;
                }
            };

            FutureTask futureTask = new FutureTask<>(callable);
            Thread thread = new Thread(futureTask);
            thread.start();

            System.out.println("1111111111111");
            System.out.println(futureTask.get());

        }
}
