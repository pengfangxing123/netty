package com.netty.thread.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 86136
 */
public class Diylisten {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Diylisten diylisten = new Diylisten();
        diylisten.test();
    }

    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<MyListen> submit = executorService.submit(() -> {
            System.out.println("6666");
        }, new MyListen());
        submit.get().toDosomthing();
    }

class MyListen{

     public void toDosomthing(){
        System.out.println("任务已经完成");
     }
}
}

