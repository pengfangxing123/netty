package com.netty.datastructure.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 * 每次遍历把最大的放到最后面
 * @author 86136
 */
public class BubbleSort {
    
    public static void main(String[] args) {
        //int[] array = {20, 10, 9, 3, -1};
        int[] array = {-1, 3, 9, 20, 10};
        for(int j=0;j<array.length-1;j++){
            int temp;
            boolean innerFlg=true;
            for(int i=0;i<array.length-1;i++){
                if(array[i]>array[i+1]){
                    temp=array[i];
                    array[i]=array[i+1];
                    array[i+1]=temp;
                    innerFlg=false;
                }
            }
            System.out.println("第"+j+"次排序："+Arrays.toString(array));
            if(innerFlg){
               break;
            }
        }
        System.out.println("最终顺序："+Arrays.toString(array));
    }
}

