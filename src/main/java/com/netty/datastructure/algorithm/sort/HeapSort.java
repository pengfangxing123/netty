package com.netty.datastructure.algorithm.sort;

import java.util.Arrays;

/**
 * 堆排序
 * @author 86136
 */
public class HeapSort {
    public static void main(String[] args) {
        int[] array={2,3,4,5,1,-1,-9,34,6,54,33,88,99,10};
        sort(array);
        System.out.println(Arrays.toString(array));
    }


    public static void sort(int[] array){
        //首先获取最后一个非叶子节点，就是最后一个叶子节点（下标为array.length-1）的父节点
        int lastIndex=((array.length-1)-1)/2;
        //这里是i-- 而不是 初次理解的(i-1)/2,是因为，所有的非叶子节点都要与它的子节点去判断大小，这样才能形成大顶堆，或小顶堆
        //如果上层节点调整位置，那么涉及到的变动只有调整后的节点，所以只要比较它的子节点就可以了
        for(int i=lastIndex; i>=0;i--){
            adjustHeap(array,i,array.length);
        }

        //上面已经得到一个大顶堆了
        //交换
        for(int j=array.length-1;j>=0;j--){
            int temp =array[j];
            array[j]=array[0];
            array[0]=temp;
            //这个时候因为堆顶的数和数组下标为j的元素交换过，破坏了大顶堆的结构
            //所以要重写构建一个大顶堆，又因为只是破坏了堆顶一个，所以只要以堆顶元素开始调整一次就可以了
            //j这里是长度，上面用的时候是下标，已经减一了，所以不要再减了
            adjustHeap(array,0,j);
        }
    }

    public static void adjustHeap(int[] array ,int i ,int length){
        int temp=array[i];

        for(int k=i*2+1;k<length;k=k*2+1){
            //先比较两个子节点的大小，得出大的
            if(k+1 < length && array[k]<array[k+1]){
                k++;
            }
            //子节点中较大值和temp比较
            //这里为什么要继续比较下去呢，因为当上层的节点调整位置时，会破坏下层的结构，就是调整后，当前节点可能比两个子节点小
            if(array[k]>temp){
                array[i]=array[k];
                i=k;
            }else {
                //这里为什么能break 不用继续比较呢，因为最开始是从最下面的的非叶子节点开始的，如果子节点不大于，那么就不需要继续比较下去
                //假如是上层阶调整后，再次进入到下层节点，这个时候下层的节点都是调整过的，一定是符合要求的，也不需要继续下去
                break;
            }
        }

        array[i]=temp;
    }
}
