package com.netty.redis;

import org.redisson.Redisson;
import org.redisson.RedissonReadLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
public class Readlock {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress("127.0.0.1:6369","127.0.0.1:6379", "127.0.0.1:6389")
                .setMasterName("masterName")
                .setPassword("password").setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        // 还可以getFairLock(), getReadWriteLock()
        RLock redLock = redissonClient.getLock("REDLOCK_KEY");
        boolean isLock;
        try {
            isLock = redLock.tryLock();
            // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
            isLock = redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
            }
        } catch (Exception e) {
        } finally {
            // 无论如何, 最后都要解锁
            redLock.unlock();
        }
    }

    /**
     * 红锁
     */
    public static void testGetredLock(){
        Config config1= new Config();
        config1.useSentinelServers().addSentinelAddress("127.0.0.1:6369","127.0.0.1:6379", "127.0.0.1:6389")
                .setMasterName("masterName")
                .setPassword("password").setDatabase(0);
        RedissonClient redissonClient1 = Redisson.create(config1);

        Config config2 = new Config();
        config2.useSentinelServers().addSentinelAddress("127.0.0.1:63692","127.0.0.1:63792", "127.0.0.1:63892")
                .setMasterName("masterName")
                .setPassword("password").setDatabase(0);
        RedissonClient redissonClient2 = Redisson.create(config2);

        Config config3 = new Config();
        config2.useSentinelServers().addSentinelAddress("127.0.0.1:63693","127.0.0.1:63793", "127.0.0.1:63893")
                .setMasterName("masterName")
                .setPassword("password").setDatabase(0);
        RedissonClient redissonClient3 = Redisson.create(config3);

        // 还可以getFairLock(), getReadWriteLock()
        RLock redLock1 = redissonClient1.getLock("REDLOCK_KEY");
        RLock redLock2 = redissonClient2.getLock("REDLOCK_KEY");
        RLock redLock3 = redissonClient3.getLock("REDLOCK_KEY");
        RedissonRedLock redissonRedLock = new RedissonRedLock(redLock1, redLock2, redLock3);
        boolean isLock;
        try {
            isLock = redissonRedLock.tryLock();
            // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
            isLock = redissonRedLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
            }
        } catch (Exception e) {
        } finally {
            // 无论如何, 最后都要解锁
            redissonRedLock.unlock();
        }
    }
}
