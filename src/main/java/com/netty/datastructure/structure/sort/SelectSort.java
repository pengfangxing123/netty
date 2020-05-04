package com.netty.datastructure.structure.sort;

import java.util.Arrays;

/**
 * 选择排序
 * 每次取最大，或最小的放在数组最前面。下次比较的时候从忽略已知的最小位
 * 和插入排序的区别是，插入排序是和前面已有的去比较，选择排序则是和前面已知的最小或最大的，然后当前值去和后面的去比较
 * @author 86136
 */
public class SelectSort {
    public static void main(String[] args) {
        int[] array = {9, -1};

        for(int i=0;i<array.length-1;i++){
            int min=array[i];
            int cur;
            for (int j=i+1;j<array.length;j++){
                if(min>array[j]){
                    cur=array[j];
                    array[j]=min;
                    min=cur;

                }
            }
            array[i]=min;
            System.out.println("第"+i+"次交换："+ Arrays.toString(array));
        }
        System.out.println("最终顺序："+Arrays.toString(array));
    }
}
