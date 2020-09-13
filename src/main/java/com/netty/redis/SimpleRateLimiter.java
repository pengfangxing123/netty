package com.netty.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;

/**
 * 利用zset限流
 * @author 86136
 */
public class SimpleRateLimiter {
    private Jedis jedis;

    public SimpleRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean isActionAllowed(String userId,String actionKey ,int period ,int maxCount) throws IOException {
        String key = String.format("hist:%s:%s", userId, actionKey);
        long nowTs = System.currentTimeMillis();
        Pipeline pipe = jedis.pipelined();
        pipe.multi();

        //插入一条当前的请求，score是当前实际
        pipe.zadd(key,nowTs,"唯一"+nowTs);

        //删掉一个周期以外的数据
        pipe.zremrangeByScore(key,0,nowTs-period*1000);
        //获取当前周期内的
        Response<Long> zcard = pipe.zcard(key);
        //给这个key重新给一个超时实际
        pipe.expire(key,period+1);
        pipe.exec();

        pipe.close();
        //判断数量是否满足
        //这里应该还有个操作，如果不满足要删掉上面插入那条数据；
        return zcard.get()<=maxCount;
    }

    public static void main(String[] args) throws IOException {
        Jedis jedis = new Jedis();
        SimpleRateLimiter limiter = new SimpleRateLimiter(jedis);
        for (int i=0;i<500;i++){
            System.out.println(limiter.isActionAllowed("userId","testAction",60,5));
        }
    }
}
