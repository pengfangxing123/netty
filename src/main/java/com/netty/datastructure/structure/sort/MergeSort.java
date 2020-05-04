package com.netty.datastructure.structure.sort;

import java.util.Arrays;

/**
 * 归并排序
 * @author 86136
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] array = {8, 4, 5, 7, 1, 4,3,6,2,-9};
        System.out.println("初始顺序："+ Arrays.toString(array));
        MergeSort.mergeSort2(array,0,array.length-1 ,new int[array.length]);
        System.out.println("最终顺序："+ Arrays.toString(array));
    }

    /**
     * 这样实虽然可以实现排序，但是效率是不太理想的，因为一次合并后不需要再全部排序了，因为两部分组内是分别有序的，
     * 而且排序要放到 if(right>left) 的里面，这样可以让只有一个元素时，不需要进行排序
     * @param array
     * @param left
     * @param right
     */
    public static void mergeSort(int[] array, int left ,int right){
        if(right>left){
            int index=(right-left)/2;
            mergeSort(array,left,left+index);
            mergeSort(array,left+index+1,right);
        }

        //选择排序
//        for(int i=left;i<(right-left+1) -1;i++){
//            int temp = array[i];
//            int cur;
//            for(int j=i+1;j<(right-left+1) ;j++){
//                if(temp>array[j]){
//                    cur=array[j];
//                    array[j]=temp;
//                    temp=cur;
//                }
//            }
//            array[i]=temp;
//        }

        //冒泡排序
//        for(int i=left;i<(right-left+1)-1 ;i++){
//            int temp;
//            for(int j=0 ;j<(right-left+1)-1; j++){
//               if(array[j]>array[j+1]){
//                    temp=array[j];
//                    array[j]=array[j+1];
//                   array[j+1]=temp;
//                }
//            }
//        }

        //插入排序
        for(int i=left+1;i<(right-left+1);i++){
            int temp=array[i];
            while (i>0&& temp<array[i-1]){
                array[i]=array[i-1];
                i--;
            }
            array[i]=temp;
        }

    }

    public static void mergeSort2(int[] array, int left ,int right,int[] tArray){
        if(right>left){
            int index=(right-left)/2;
            System.out.println("左边，左："+left+",右："+(left+index));
            mergeSort2(array,left,left+index,tArray);

            System.out.println("右边，左："+(left+index+1)+",右："+right);
            mergeSort2(array,left+index+1,right,tArray);
            merge(array,left,right,left+index,tArray);
        }


    }

    private static void merge(int[] array, int left, int right, int index, int[] tArray) {
        //System.out.println("左："+left+",右："+right);
        int l=left,r=index+1;
        int t=0;

        while (l<=index&& r<=right){
            if(array[l]<=array[r]){
                tArray[t]=array[l];
                t++;
                l++;
            }else{
                tArray[t]=array[r];
                t++;
                r++;
            }
        }

        while (l<=index){
            tArray[t]=array[l];
            l++;
            t++;
        }

        while (r<=right){
            tArray[t]=array[r];
            r++;
            t++;
        }

        for(int i=left, j=0; i<=right;i++,j++){
            array[i]=tArray[j];
        }
    }
}
