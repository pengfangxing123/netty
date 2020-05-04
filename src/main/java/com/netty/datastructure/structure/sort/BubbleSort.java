package com.netty.datastructure.structure.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 * 每次遍历把最大的放到最后面
 * @author 86136
 */
public class BubbleSort {
    
    public static void main(String[] args) {
        //int[] array = {20, 10, 9, 3, -1};
        int [] array = {8, 9,1, 7, 2, 3, 5, 4, 6, 0};
//        for(int j=0;j<array.length-1;j++){
//            int temp;
//            boolean innerFlg=true;
//            for(int i=0;i<array.length-1;i++){
//                if(array[i]>array[i+1]){
//                    temp=array[i];
//                    array[i]=array[i+1];
//                    array[i+1]=temp;
//                    innerFlg=false;
//                }
//            }
//            System.out.println("第"+j+"次排序："+Arrays.toString(array));
//            if(innerFlg){
//               break;
//            }
//        }
//        System.out.println("最终顺序："+Arrays.toString(array));
        BubbleSort bubbleSort = new BubbleSort();
        bubbleSort.sort2(array);
    }


    /**
     * 向前排序,其实和插入排序差不多，只是一个用wihle ，一个用 for break 结束，
     * 然后 插入排序把要比较的元素提取出来了(这也是用while 所必须的)，用一个index，指向元素暂时在的位置，
     * 而这里是直接互相交换了
     * @param array
     */
    public void sort2(int[] array){
        for(int i=0;i<array.length;i++){
            for(int j=i;j>0;j-=1){
                if(array[j]<array[j-1]){
                    int temp=array[j];
                    array[j]=array[j-1];
                    array[j-1]=temp;
                }else{
                    break;
                }
            }
        }

        System.out.println("最终顺序："+Arrays.toString(array));
    }
}

