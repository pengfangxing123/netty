package com.netty;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * @author 86136
 */
public class MyStop {
    private static final Logger logger = LoggerFactory.getLogger(MyStop.class);
    public static void main(String[] args) throws InterruptedException {
        StopWatch sw = new Slf4JStopWatch();
        TimeUnit.SECONDS.sleep(3);
        sw.setTag("setTag");
        TimeUnit.SECONDS.sleep(3);
        sw.stop("6666","777");
        System.out.println("上面stop也会打印耗时，以及设置的tag，message，最终耗时"+sw.getElapsedTime());




    }
}
