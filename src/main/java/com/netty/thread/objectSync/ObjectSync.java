package com.netty.thread.objectSync;

import java.util.List;
//import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author 86136
 */
public class ObjectSync {
    private Object[] locks;
    private int size;
    private List datas;
    private ExecutorService pool=
            new ThreadPoolExecutor(10,10,0, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

    public ObjectSync(int size ,List datas) {
        this.size = size;
        locks=new Object[size];
        IntStream.range(0,size).boxed().forEach(p->locks[p]=new Object());
        this.datas=datas;
    }

    public void dealData(){

    }

    public Object[] getLocks() {
        return locks;
    }


    public static void main(String[] args) {
        List<Integer> collect = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        ObjectSync objectSync = new ObjectSync(10,collect);
        objectSync.dealData();

    }
}
