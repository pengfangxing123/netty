package com.netty.collect;

import com.netty.innnerclass.InterfaceCongfig;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
public class MapTest {
    public static void main(String[] args) {
//        ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
//        map.put("string","string");
//        map.put("str","str");
//        String s = map.get("string");
//        System.out.println(s);
//
//        System.out.println(Integer.MAX_VALUE - 8);
//        System.out.println(1 << 30);
//
//        Set<String> set=new HashSet<>();
//        set.iterator()
        repeat(10, System.out::println);
        Stream.of("1","2").collect(Collectors.toList()).
                stream().reduce(new StringBuilder(), (sb, s) -> sb.append(s).append(','), StringBuilder::append).toString();
    }

    public static void repeat(int n, IntConsumer action){
        for(int i=0;i<n;i++){
            action.accept(i);
        }
    }

}
