package com.netty.datastructure.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 86136
 */
public class TowSum {
    public static void main(String[] args) {
        int[] arr ={1,2,3,4,5,6,7,8,9,10};
        int val=10;

        Map<Integer,Integer> map = new HashMap<>();
        for (int value : arr) {
            int div = val - value;
            if (map.containsValue(div)) {
                System.out.println(map.get(value) + " " + value);
            } else {
                map.put(div, value);
            }
        }
    }
}
