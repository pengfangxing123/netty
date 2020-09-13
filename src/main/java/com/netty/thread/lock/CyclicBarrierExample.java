package com.netty.thread.lock;

import java.util.Set;
import java.util.concurrent.*;

/**
 * CyclicBarrier(int parties)//等待parties个线程到达同一点
 * CyclicBarrier(int parties,Runnable barrierAction)//等待parties个线程到达同一点后执行barrierAction
 * @author 86136
 */
public class CyclicBarrierExample implements Runnable{

    private ExecutorService threadPool     = Executors.newFixedThreadPool(3);

    private CyclicBarrier cb = new CyclicBarrier(3, this);

    private ConcurrentHashMap<String, Integer> map            = new ConcurrentHashMap<>();

    private void count() {
        for (int i = 0; i < 3; i++) {
            threadPool.execute(() -> {
                //计算每个学生的平均成绩,代码略()假设为60~100的随机数
                int score = (int) (Math.random() * 40 + 60);
                try {
                    Thread.sleep(Math.round(Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                map.put(Thread.currentThread().getName(), score);
                System.out.println(Thread.currentThread().getName() + "同学的平均成绩为" + score);
                try {
                    //执行完运行await(),等待所有学生平均成绩都计算完毕
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void run() {
        int result = 0;
        Set<String> set = map.keySet();
        for (String s : set) {
            result += map.get(s);
        }
        System.out.println("三人平均成绩为:" + (result / 3) + "分");
    }

    public static void main(String[] args) throws InterruptedException {
        long now = System.currentTimeMillis();
        CyclicBarrierExample cb = new CyclicBarrierExample();
        cb.count();
        Thread.sleep(100);
        long end = System.currentTimeMillis();
        System.out.println(end - now);
        //可重复使用
        cb.cb.reset();
        cb.count();
        Thread.sleep(100);
        long end2 = System.currentTimeMillis();
        System.out.println(end - now);
        cb.threadPool.shutdown();
    }

}
