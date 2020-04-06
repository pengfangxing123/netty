package com.netty.datastructure.recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * 八皇后问题 回溯算法
 * @author 86136
 */
public class Queue8 {
    private int max=8;
    private int[] array=new int[max];
    public List<int[]> list=new ArrayList();

    private void print(){
        for(int i=0;i<array.length;i++){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }

    /**
     * 判断第n个皇后，和之前的皇后是否右冲突
     * @param n
     * @return
     */
    private boolean isConflict(int n){
        for(int i=0;i<n;i++){
            if(array[i]==array[n] || Math.abs(n-i)==Math.abs(array[n]-array[i])){
                return false;
            }
        }
        return true;
    }

    private void check(int n){
        //n=8,表示前面已经放了8个了
        if(n == max){
            int[] cur = new int[8];
            System.arraycopy(array,0,cur,0,array.length);
            list.add(cur);
            print();
            return;
        }

        // 这里的max是列的数量，
        // 当n=8，表示找到位置会return，回到第八层,第八层还在循环中，需要在该列还要继续往下走，看剩下的格子中是否右满足要求的位置
        // 当最后一层已经没有满足的时，会return 回到第上一层第七层，第七层还在循环中，会继续在后面的列中查找，
        // 找到该层合适的，则继续跳到下一层第八层，再在下一层一个个的找，如果找到会进入到第九层，这时n=8.会return，返回第八层。
        // 然后会继续剩下的循环，再第八层找，直到第八层循环结束，回到第七层。然后再像上面一样，直到第七层列遍历结束，回到第六层。。。
        // 直到回到第一层，且遍历结束,这就是回溯算法
        for(int i =0;i<max;i++){
            //先把当前这个皇后n，从第一列开始放
            array[n]=i;
            //判断是否冲突
            if(isConflict(n)){
                //不冲突
                check(n+1);
            };
        }
    }


    public static void main(String[] args) {
        Queue8 queue8 = new Queue8();
        queue8.check(0);
        int size = queue8.list.size();
        System.out.println(size);
    }

}
