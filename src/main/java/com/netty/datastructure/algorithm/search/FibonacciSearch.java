package com.netty.datastructure.algorithm.search;

/**
 * 将数组扩容成 斐波那契数列中元素的大小，然后找到黄金分割点  比如长度为13，那么他的分割点 就在第8位
 *  分割点mid=low+F(k-1)-1
 *  1，F[k]=F[k-1]+F[k-2]可以得知，可以得到F[k]-1=(F[k-1]-1)+(F[k-2]-1)+1,
 *  这个式子说明只要是顺序表的长度为F[k],就可以分为(F[k-1])和(F[k-2])的长度，那么用下标表示就是
 *  F[k]-1=(F[k-1]-1)+(F[k-2]-1)+1,另外一个1就是mid位置的元素
 *  这个F[k]-1其实就是下标，F[k]长度的数组，最后一位的下标是F[k]-1；
 *
 *  2，其实没必要像 1那样去理解，首先 我们扩容后的数组长度为F[k],那么该数组的黄金分割点是第F[k-1]位，该位的下标就为F[k-1]-1,
 *  所有就有mid=low+F(k-1)-1
 * 斐波那契 查找法
 * @author 86136
 */
public class FibonacciSearch {
    public static void main(String[] args) {
        int[] array=new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
        int i = fibSearch(array, 12);
        System.out.println("找到元素，下标="+i );

    }

    /**
     * 得到一个斐波那契数列
     * @param maxSize
     * @return
     */
    public static int [] fib(int maxSize){
        int[] f=new int[maxSize];
        f[0]=1;
        f[1]=1;
        for (int i=2;i<maxSize;i++){
            f[i]=f[i-1]+f[i-2];
        }
        return f;
    }

    public static int fibSearch(int[] a,int key){
        int length = a.length;
        int[] f = fib(20);

        //将数组长度扩容至斐波那契数列中元素的长度
        int k = 0;
        while (length > f[k]) {
            k++;
        }

        //得到创建后的数组
        int[] array = new int[f[k]];
        System.arraycopy(a, 0, array, 0, a.length);

        //用原数组最后的一位的值补齐新数组
        for(int i=length;i<array.length;i++){
            array[i]=a[length-1];
        }

        int left=0;
        int mid;
        int right=a.length-1;

        while (left <= right){
            //得到mid
            mid=left+f[k-1]-1;

            if(key<array[mid]){
                //注意这时候right-left已经不等于斐波那契数列元素的长度了，因为mid-1,但是这个不影响了，
                // 因为就算少一位，再扩容一位等于斐波那契数列元素的长度的话,mid 取值还是同样的值
                right=mid-1;
                k--;
            }else if(key>array[mid]){
                left=mid+1;
                k-=2;
            }else{
                if(right<=mid){
                    return right;
                }else{
                    return mid;
                }
            }
        }
        return -1;

    }
}
