package com.netty.thread.threadpool;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 86136
 */
public class SimpleTreadPool extends Thread{
    private int size;

    private final int queueSize;

    private final DiscardPolicy discardPolicy;

    private final static int DEAFULT_SIZE=10;

    private final static int DEFAULT_TASK_QUEUE_SIZE=40;

    private final static LinkedList<Runnable> TASK_QUEQUE =new LinkedList<>();

    private static volatile int seq=0;

    private final static String THREAD_PRRFIX="SIMPLE_THREAD_POOL-";

    private final static ThreadGroup GROUP=new ThreadGroup("Pool_group");

    private final static DiscardPolicy DEFAULT_DISCARD_POLICY=()->{
        throw new RuntimeException("队列已满");
    };

    private volatile boolean destroy =false;

    private int min;

    private int max;

    private int active;

    private final static List<WorkerTask> THREAD_QUEQUE=new ArrayList<>();
    public SimpleTreadPool (){
        this(4,8,12,DEFAULT_TASK_QUEUE_SIZE,DEFAULT_DISCARD_POLICY);
    }

    public SimpleTreadPool (int min, int active, int max,int queueSize ,DiscardPolicy discardPolicy){
        this.min=min;
        this.active=active;
        this.max=max;
        this.queueSize=queueSize;
        this.discardPolicy=discardPolicy;
        init();
    }

    private void init() {
        for(int i=0;i<this.min;i++){
            createWorkTask();
        }
        this.size=this.min;
        this.start();
    }

    @Override
    public void run() {
        while (!destroy){
            System.out.println("Pool#min:"+min+",Active:"+active+",max:"+max+",Current:"+size+",queueSize:"+queueSize);
            try {
                Thread.sleep(10000);
                if(TASK_QUEQUE.size()>active&& size<active){
                    for(int i=size;i<active;i++){
                        createWorkTask();
                    }
                    System.out.println("the pool incremented,");
                    this.size=active;
                }else if(TASK_QUEQUE.size()>max&& size<max){
                    for(int i=size;i<max;i++){
                        createWorkTask();
                    }
                    System.out.println("the pool incremented to max");
                    this.size=max;
                }

                //对THREAD_QUEQUE加锁是为了防止shutDown遍历时把workerTask给remove掉了
                synchronized (THREAD_QUEQUE) {
                    if (TASK_QUEQUE.isEmpty() && size > active) {
                        int releaseSize = size - active;
                        for (Iterator<WorkerTask> it = THREAD_QUEQUE.iterator(); it.hasNext();) {
                            if (releaseSize <= 0) {
                                break;
                            }
                            WorkerTask next = it.next();
                            next.close();
                            next.interrupt();
                            it.remove();
                            releaseSize--;
                        }
                        size = active;
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createWorkTask(){
        WorkerTask workerTask = new WorkerTask(GROUP, THREAD_PRRFIX + (seq++));
        workerTask.start();
        THREAD_QUEQUE.add(workerTask);
    }

    public void shutdown() throws InterruptedException {
        while ((!TASK_QUEQUE.isEmpty())){
            Thread.sleep(50);
        }
        int initVal=THREAD_QUEQUE.size();

        while (initVal>0){
            for(WorkerTask workerTask : THREAD_QUEQUE){
                if(workerTask.taskState==TaskState.BLOCKED){
                    workerTask.interrupt();
                    workerTask.close();
                    initVal--;
                }else{
                    Thread.sleep(10);
                }
            }
        }
        this.destroy=true;
        System.out.println("the thread pool is destroy");
    }

    public int getQueueSize(){
        return queueSize;
    }

    public int getSize(){
        return size;
    }

    public boolean isDestroy(){
        return destroy;
    }

    private void submit(Runnable runnable){
        if(isDestroy()){
            throw  new IllegalStateException("The thread pool already destroy");
        }
        synchronized (TASK_QUEQUE){
            if(TASK_QUEQUE.size()>queueSize){
                discardPolicy.discard();
            }
            TASK_QUEQUE.add(runnable);
            TASK_QUEQUE.notifyAll();
        }
    }

    public interface DiscardPolicy{
        void discard () throws RuntimeException;
    }

    private static class WorkerTask extends Thread{
        private volatile TaskState taskState=TaskState.FREE;

        public WorkerTask(ThreadGroup group,String name){
            super(group,name);
        }

        public TaskState getTaskState(){
            return this.taskState;
        }

        @Override
        public void run(){
            OUTER:
            while ((this.taskState!=TaskState.DEAD)){
                Runnable runnable;
                synchronized (TASK_QUEQUE){
                    while (TASK_QUEQUE.isEmpty()){
                        try {
                            this.taskState=TaskState.BLOCKED;
                            TASK_QUEQUE.wait();
                        } catch (InterruptedException e) {
                            break OUTER;
                        }
                    }
                }
                if(!CollectionUtils.isEmpty(TASK_QUEQUE)){
                    runnable = TASK_QUEQUE.removeFirst();
                    if(runnable!=null){
                        this.taskState=TaskState.RUNNING;
                        //注意这里是run 而不是start，没有额外再开启线程
                        runnable.run();
                        this.taskState=TaskState.FREE;
                    }
                }
            }
        }

        public void close(){
            this.taskState=TaskState.DEAD;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleTreadPool simpleTreadPool = new SimpleTreadPool();
        for(int i=0; i<40;i++){
            simpleTreadPool.submit(()->{
                System.out.println("The runable be serviced by"+ Thread.currentThread());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runable be serviced by"+ Thread.currentThread()+"finished.");
            });
        }

        Thread.sleep(10000);
        simpleTreadPool.shutdown();

    }
}

