package com.netty.datastructure.algorithm.sort;

import java.util.Arrays;

/**
 * 基数排序
 * @author 86136
 */
public class RadixSort {
    private int max;

    public static void main(String[] args) {
        int [] array = {8, 90,1, 765, 2, 344, 5, 422, 6, 0,1,12,10,4};
        //int [] array = {8, 8,8,8,8};
        System.out.println("初始："+ Arrays.toString(array));
        int[][] bucket = new int[10][array.length];
        int[] eleCount = new int[10];

        RadixSort sort = new RadixSort(1000);
        sort.sort(array,bucket,eleCount,1);
    }

    public RadixSort(int max) {
        this.max = max;
    }


    public int getMax(int[] array){
        int max=array[0];
        for (int i=1;i<array.length;i++){
            if(array[i]>max){
                max=array[i];
            }
        }
        return (max+"").length();
    }

    /**
     * 排序
     * 这里不一定要用递归，可以先计算出需要根据数组类最大数的长度，用for循环执行
     * @param array 数组
     * @param bucket 桶 二维数组用来存放元素，因为是数字的 每一位为0~9，所以是10个桶，且一个桶中最多放array.length个，
     *               所以是 new int[10][array.length];
     * @param eleCount 记录每个桶的数量，桶的数量为10，所以为int[10]
     * @param counts 用来记录 到第几位了
     */
    public void sort(int[] array, int[][] bucket, int[] eleCount, int counts){
        if(counts>=max){
            return;
        }

        //先将数据放到桶中，并记录每个桶里放了多少元素
        for(int i=0;i<array.length;i++){
             int ele = array[i]/counts%10;
             bucket[ele][eleCount[ele]++]=array[i];
        }

        //将数据从桶里取出，并将记录每个桶 中元素置为0
        for(int i=0,index=0;i<eleCount.length;i++){
            if(eleCount[i]>0){
                for(int j=0;j<eleCount[i];j++,index++){
                    array[index]=bucket[i][j];
                }
                eleCount[i]=0;

            }
        }

        System.out.println("排序："+ Arrays.toString(array));

        sort(array,bucket,eleCount,counts*10);
    }
}
