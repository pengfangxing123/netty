package com.netty.datastructure.algorithm.sort;

import java.util.Arrays;

/**
 * 插入排序
 * 和冒泡排序的区别是，冒泡排序每次都需要互相交换，然后每个值都是相邻两个比，
 * 而插入排序则是一直和给定的值比较，然后碰到不满足条件直接结束
 * @author 86136
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] array = {9,-1, 9,3, 20, 10};

        for(int i=1;i<array.length;i++){
            int cur = array[i];
            int lastIndex=i-1;

            while (lastIndex>=0 && cur<array[lastIndex]){
                //当前的值，要大于上一位的值，那么要交换位置,
                // 这里要用array[lastIndex+1]表示当前位，因为改值会向前移动
                array[lastIndex+1]=array[lastIndex];
                //这里是没必要的。因为一直是拿cur的值去比较，只要再比较完毕后，将cur值放到正确的位置即可
                //array[lastIndex]=cur;
                //继续与前面更小的值比较
                lastIndex--;
            }
            //放入正确的位置
            array[lastIndex+1]=cur;

            System.out.println("第"+i+"次插入："+ Arrays.toString(array));
        }
        System.out.println("最终顺序："+Arrays.toString(array));
    }
}
