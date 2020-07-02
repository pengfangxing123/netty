package com.netty.collect.concurrent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 86136
 */
public class ConcurrentHashMapStart {
    public static void main(String[] args) {
        ConcurrentHashMap<String,Object> map=new ConcurrentHashMap<>();
        map.put("testInint","testInint-val");
        int i=32795;
        int j =i<<16;
        System.out.println(j);
    }
}
