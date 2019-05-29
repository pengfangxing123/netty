package com.netty.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
public class ThreadStatus {

    public static void main(String[] args) {
        Executor threadPool = Executors.newFixedThreadPool(10);
        //threadPool.execute();

        List<String> integers = Arrays.asList("111", "22");
        integers.forEach(p->{
            System.out.println(p);
        });

        integers.parallelStream().forEach(p->{
            System.out.println(p);
        });

        new Thread(()-> {
            System.out.println("11111");
            System.out.println("122222");
        }
        ).start();
        integers.stream().filter(p->{
           if(p.equals("111"))
               return true;
           return false;
        }).collect(Collectors.toList());
    }
}
