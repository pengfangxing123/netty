package com.netty.datastructure.algorithm.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分查找法
 * @author 86136
 */
public class SecondSearch {
    public static void main(String[] args) {
        SecondSearch search = new SecondSearch();
        int[] array=new int[]{1,2,3,4,5,6,8,20,20,40,40,40};
        //List<Integer> list = search.recursiveSearch2(array, 40, 0, array.length - 1);
        //System.out.println(list);
        System.out.println(search.search(array,1));
    }

    /**
     * 递归找单个
     * @param array
     * @param findVal
     * @param left
     * @param right
     */
    public void recursiveSearch(int[] array,int findVal ,int left, int right){
        if(left>right){
            System.out.println("无法找到该元素："+findVal);
            return ;
        }
        int index=(left+right)/2;
        if(findVal==array[index]){
            System.out.println("找到元素，value="+array[index]+",index="+index);
            return;
        }

        if(findVal<array[index]){
            recursiveSearch(array,findVal,left,index-1);
        }

        if(findVal>array[index]){
            recursiveSearch(array,findVal,index+1,right);
        }

    }

    /**
     * 非递归找单个
     * @param array
     * @param findVal
     */
    public int search(int [] array,int findVal){
        int left = 0;
        int mid;
        int right=array.length-1;
        while (left<=right){
            mid=(left+right)/2;
            if(findVal<array[mid]){
                right=mid-1;
            }else if(findVal>array[mid]){
                left=mid+1;
            }else{
                return mid;
            }
        }
        return -1;
    }

    /**
     * 找多个
     * @param array
     * @param findVal
     * @param left
     * @param right
     * @return
     */
    public List<Integer> recursiveSearch2(int[] array, int findVal , int left, int right){
        if(left>right){
            System.out.println("无法找到该元素："+findVal);
            return new ArrayList<>();
        }
        int index=(left+right)/2;
        if(findVal==array[index]){
            System.out.println("找到元素，value="+array[index]+",index="+index);
            List<Integer> list = new ArrayList<>();
            list.add(index);

            //将左边所有相等的放入list
            int temp=index -1;
            while (temp>=0 && findVal==array[temp]){
                list.add(temp);
                temp--;
            }

            //将右边相等的放入list
            temp=index+1;
            while (temp<=array.length-1 && findVal==array[temp]){
                list.add(temp);
                temp++;
            }
            return list;

        }else if(findVal<array[index]){
            return recursiveSearch2(array,findVal,left,index-1);
        }else {
            return recursiveSearch2(array,findVal,index+1,right);
        }

    }
}
