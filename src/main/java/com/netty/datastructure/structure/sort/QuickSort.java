package com.netty.datastructure.structure.sort;

import java.util.Arrays;

/**
 * 快速排序
 * 冒泡排序的改进
 * @author 86136
 */
public class QuickSort {
    public static void main(String[] args) {
        int [] arr = {5,6,6,6,5,6,6,7, 5,5};
        System.out.println(Arrays.toString(arr));
        System.out.println("##########开始##################");
        quickSort(arr, 0, arr.length-1);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]+" ");
        }

    }

    /**
     * 已中间为基准
     * @param arr
     * @param left
     * @param right
     */
    private static void quickSort(int[] arr,int left, int right){
        //左下标
        int l = left;
        //右下标
        int r = right;
        //pivot 中轴值
        int pivot = arr[(left + right) / 2];
        //临时变量，作为交换时使用
        int temp = 0;
        //while循环的目的是让比pivot 值小放到左边
        //比pivot 值大放到右边
        while( l < r) {
            //在pivot的左边一直找,找到大于等于pivot值,才退出
            while( arr[l] < pivot) {
                l += 1;
            }
            //在pivot的右边一直找,找到小于等于pivot值,才退出
            while(arr[r] > pivot) {
                r -= 1;
            }
            //如果l >= r说明pivot 的左右两的值，已经按照左边全部是
            //小于等于pivot值，右边全部是大于等于pivot值
            if( l >= r) {
                break;
            }

            //交换
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;

            //1，下面两个操作，使为了防止等于基数而一直没有l和r一直停留在原位置的
            //2，因为没有做基数和最后l和r指向的元素交换，那么基数要一直在l和r的中间(假如不在中间array[l]和arry[r]的交换就没有意义)
            //3，而且如果前面与基数比较 l++ 或r-- 等于基数是不成立的，假如一直两个都等于基数将，将死循环，那么就需要进一位，但
            // 是如果array[l]或者array[r]等于基数,都进一位的话，将会使第二点不成立，array[l]等于基数时，r--;array[r]等于基数时，l++

            //如果交换完后，发现这个arr[l] == pivot值 相等 r--， 前移
            if(arr[l] == pivot) {
                r -= 1;
            }
            //如果交换完后，发现这个arr[r] == pivot值 相等 l++， 后移
            if(arr[r] == pivot) {
                l += 1;
            }
        }

        // 如果 l == r, 必须l++, r--, 否则为出现栈溢出
        if (l == r) {
            l += 1;
            r -= 1;
        }
        //打印每次分组
        System.out.println("共工："+Arrays.toString(arr));
        int length1 = r  - left +1;
        int[] ints = new int[length1];
        System.arraycopy(arr,left,ints,0,length1);
        System.out.println("左边："+Arrays.toString(ints));

        int length2 = right - l +1;

        int[] intr = new int[length2];
        System.arraycopy(arr,l,intr,0,length2);
        System.out.println("右边："+Arrays.toString(intr));
        //向左递归
        if(left < r) {
            quickSort(arr, left, r);
        }
        //向右递归
        if(right > l) {
            quickSort(arr, l, right);
        }

    }

    /**
     * 以最左边为基准
     * @param array
     * @param left
     * @param right
     */
    public static void quickSort2(int[] array,int left,int right){
        if(left >= right){
            return;
        }
        int l=left,r=right;
        //基准
        int temp= array[left];
        while (l<r){
            //先看右边，依次往左递减
            //因为基准在左边，while循环结束后要将 将最后的中间位置，也就是最后r,l执行的元素与基准交换，
            // 而基准是最左边的，r,l指向的元素必须小于等于基准，而从右边开始最后r指向的元素肯定会小于等于基准
            // 然后在 arry[l]与temp的遍历比较时，因为l<r的限制，会最终使r 和 l指向的是一个小于等于基准的数
            // 如果基准在右边，那么就有从左边开始了
            while (array[r]>=temp&&l<r){
                r--;
            }

            //再看左边，依次往右递增
            while(array[l]<=temp&&l<r){
                l++;
            }

            if(l==r){
                break;
            }

            temp=array[l];
            array[l]=array[r];
            array[r]=temp;
        }

        //最后将基准为与i和j相等位置的数字交换
        array[left]=array[l];
        array[l]=temp;

        //打印每次分组
//        System.out.println("共工："+Arrays.toString(array));
//        int length1 = r - 1 - left +1;
//        int[] ints = new int[length1];
//        System.arraycopy(array,left,ints,0,length1);
//        System.out.println("左边："+Arrays.toString(ints));
//
//        int length2 = right - r - 1 +1;
//
//        int[] intr = new int[length2];
//        System.arraycopy(array,r+1,intr,0,length2);
//        System.out.println("右边："+Arrays.toString(intr));

        quickSort2(array,left,l-1);
        quickSort2(array,l+1,right);
    }
}
