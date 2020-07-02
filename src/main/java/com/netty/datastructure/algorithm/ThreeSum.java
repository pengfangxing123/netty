package com.netty.datastructure.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1、新建集合
 * 2、应当判定的只要数组的长度小于三就直接返回空集合
 * 3、然后对数组进行排序（不进行排序是无序数组，遍历查找工作相当复杂，而且效率极低）
 * 4、遍历排好序的数组，外层for循环，内层双指针移动的方法，将外层循环的数和双指针指向的数相加，只要相同，就存入存值集合，之后指针分别向中间移动，要判断如果指针指向的前一个数和后一个数相同那么继续移动指针
 * 5、注意：外层只要遍历到了某个数大于0，那么就直接返回此时的存值集合（因为是排好序的集合，只要大于0了，后面怎么遍历都不会等于0）
 * @author 86136
 */
public class ThreeSum {
    public static void main(String[] args) {
        int[] arrays = new int[] {-2,0,1,1,2};
        System.out.println(getArraysSort(arrays));

    }
    public static List<List<Integer>> getArraysSort(int[] arrays) {
        List<List<Integer>> returnList = new ArrayList<List<Integer>>();
        // 获取数组长度
        int length = arrays.length;
        if(length < 3) {
            return returnList;
        }
        // 数组排序
        Arrays.sort(arrays);
        // 采用指针进行遍历，找值
        for(int i = 0;i < length;i++) {
            // 判断剩余的数是否都为正数
            if(arrays[i] > 0) {
                return returnList;
            }
            // 判断数字是否重复
            if(i > 0 && arrays[i] == arrays[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = length - 1;
            while(left < right) {
                int temp = arrays[i] + arrays[left] + arrays[right];
                if(temp == 0) {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(arrays[i]);
                    list.add(arrays[left]);
                    list.add(arrays[right]);
                    returnList.add(list);
                    left++;
                    right--;
                    while(left < right && arrays[left] == arrays[left - 1]){
                        left++;
                    }
                    while(left < right && arrays[right] == arrays[right + 1]){
                        right--;
                    }
                    continue;
                }else if(temp < 0) {
                    left++;
                    continue;
                }else {
                    right--;
                    continue;
                }

            }
        }

        return returnList;
    }
}
