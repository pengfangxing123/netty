package com.netty.datastructure.structure.search;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 插值算法
 * 数据量大，元素大小分布均匀的情况下 速度比较块
 * 在元素大小分布不均匀的情况下，可能会比较二分法更慢
 * @author 86136
 */
public class InsertValueSearch {
    private AtomicInteger conut=new AtomicInteger();

    public static void main(String[] args) {
        InsertValueSearch search = new InsertValueSearch();
        int[] array=new int[]{1,2,3,4,5,6,8,20,20,40,40,40};
        search.search(array, 2, 0, array.length - 1);
        System.out.println("查找了"+search.conut.get()+"次");
    }

    /**
     * 找单个
     * @param array
     * @param findVal
     * @param left
     * @param right
     */
    public void search(int[] array,int findVal ,int left, int right){
        conut.incrementAndGet();
        if(left>right || findVal<array[left] || findVal >array[right]){
            System.out.println("无法找到该元素："+findVal);
            return ;
        }

        //与二分法主要的区别
        // 二分法 是 left+(right-left)*1/2 找到二分的位置
        // 插值算法 是left +(rigth-left)*(findVal-array[left])/(array[right]-array[left])，
        // 因为数组是有序的，通过findVal-array[left],来获取findval更容易落在哪个区域
        int index=left + (findVal-array[left])/(array[right]-array[left]) * (right-left);

        if(findVal==array[index]){
            System.out.println("找到元素，value="+array[index]+",index="+index);
        }else if(findVal<array[index]){
            search(array,findVal,left,index-1);
        }else{
            search(array,findVal,index+1,right);
        }
    }
}
