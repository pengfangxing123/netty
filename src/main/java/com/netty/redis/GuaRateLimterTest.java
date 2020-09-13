package com.netty.redis;

import java.util.stream.IntStream;

/**
 * @author 86136
 */
public class GuaRateLimterTest {
    public static void main(String[] args) {
        GuaRateLimter rateLimter = GuaRateLimter.create(10);
        IntStream.rangeClosed(0,5).boxed().forEach(p->rateLimter.acquire());
    }
}
