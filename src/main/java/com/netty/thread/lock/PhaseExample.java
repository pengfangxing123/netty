package com.netty.thread.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * @author 86136
 */
public class PhaseExample {
    private static final Logger log = LoggerFactory.getLogger(PhaseExample.class);

    private static final ExecutorService es = Executors.newFixedThreadPool(5);

    private static void executePhaser1(Phaser phaser) {
        es.submit(new HouseholdJob(phaser, "打扫主卧"));
        es.submit(new HouseholdJob(phaser, "打扫次卧"));
        es.submit(new HouseholdJob(phaser, "打扫客厅"));
        es.submit(new HouseholdJob(phaser, "打扫卫生间"));
        es.submit(new HouseholdJob(phaser, "打扫阳台"));
        peekPhaserState(phaser, "召唤5个影分身打扫5个房间，都给我动起来！查看中间状态");
        phaser.arriveAndAwaitAdvance();
        peekPhaserState(phaser, "打扫完成了，满意！换批影分身来弄个3菜1汤吧");
    }

    private static void peekPhaserState(Phaser phaser, String s) {

    }


    private static void executePhaser2(Phaser phaser) {
        es.submit(new HouseholdJob(phaser, "炒青菜"));
        es.submit(new HouseholdJob(phaser, "红烧肉"));
        es.submit(new HouseholdJob(phaser, "油焖大虾"));
        es.submit(new HouseholdJob(phaser, "番茄鸡蛋汤"));
        phaser.arriveAndAwaitAdvance();
        peekPhaserState(phaser, "菜炒好了，看起来还不错，要喊老婆孩子出来吃饭了");
    }

    private static void executePhaser3(Phaser phaser) {
        es.submit(new HouseholdJob(phaser, "喊老婆出来吃饭"));
        es.submit(new HouseholdJob(phaser, "喊孩子出来吃饭"));
        phaser.arriveAndAwaitAdvance();
        peekPhaserState(phaser, "看看都吃完了没有，等会还得洗碗，我就是个工具人......");
    }

    private static void executePhaser4(Phaser phaser) {
        es.submit(new HouseholdJob(phaser, "洗碗"));
        phaser.arriveAndAwaitAdvance();
        peekPhaserState(phaser, "来看看洗的干不干净。");
    }



    public static void main(String[] args) throws NoSuchFieldException, InterruptedException {
        // 传入parties为1，表示注册当前线程，当前线程可作为协调线程参与工作。
        Phaser phaser = new Phaser(1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return super.onAdvance(phase, registeredParties);
            }
        };
        log.info("周末起床了，开始家务了......");
        peekPhaserState(phaser, "刚创建好一个Phaser，默认parties为1，表示我carry全场");
        executePhaser1(phaser);
        executePhaser2(phaser);
        executePhaser3(phaser);
        //executePhaser4(phaser);
        phaser.arriveAndDeregister();
        // 完成所有工作了，老夫要休息了
        phaser.forceTermination();
        peekPhaserState(phaser, "哎呀，肚子叫了，居然饿醒了，看来最近火影看太多了啊～");
    }

    private static class HouseholdJob implements Runnable {
        private final Phaser phaser;
        private final String name;

        private HouseholdJob(Phaser phaser, String name) {
            this.name = name;
            this.phaser = phaser;
            phaser.register();
        }

        @Override
        public void run() {
            //标记一个到达，且等待其他完成
            phaser.arriveAndAwaitAdvance();
            log.info("开始{}...", name);
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("完成{}.", name);
            //因为HouseholdJob创建的时候register了，而且后续每个都会regist，所以要取消regist，
            // 不然后续的arriveAndAwaitAdvance要总数到达才能唤醒
            // 大家都完成后自己取消登记（影分身消失）
            phaser.arriveAndDeregister();
        }
    }

}


