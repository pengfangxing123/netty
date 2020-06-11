package com.netty.thread.threadpool;

import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author 86136
 */
public class ExecutorCompletionServiceExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test();
    }

    private static void test() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        ExecutorCompletionService<String> executorCompletionService = new ExecutorCompletionService<>(executorService);
        IntStream.range(1,10).boxed().forEach(p->{
            executorCompletionService.submit(new MyCallable(){
                @Override
                String dosomething() throws InterruptedException {
                    TimeUnit.SECONDS.sleep(p);
                    System.out.println("666"+Thread.currentThread().getName());
                    return "执行成功："+p;
                }
            });

        });
        Future<String> future;
        while ((future=executorCompletionService.take())!=null){
            System.out.println(future.get());
        }
        //这里不会打印take会阻塞
        System.out.println("over");

    }
}

abstract class MyCallable implements Callable<String>{
    private String state;

    @Override
    public String call() throws Exception {
        String s = null;
        try {
            s = dosomething();
            delstate("success");
        } catch (Exception e) {
            e.printStackTrace();
            delstate("error");
        }

        return s;
    }

    private void delstate(String state) {
        state=state;
    }

    abstract String dosomething() throws InterruptedException;
}
