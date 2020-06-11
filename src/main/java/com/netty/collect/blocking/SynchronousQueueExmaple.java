package com.netty.collect.blocking;

/**
 * 没有容量，取值的操作，需要依赖插入操作进行；插入操作要依赖取值操作进行
 * 因为上面这种特性 所以要用阻塞的方法 put take这些
 * 也就是这个方法transferer.transfer(e, false, 0)，第二个参数为false的
 * @author 86136
 */
public class SynchronousQueueExmaple {

}
