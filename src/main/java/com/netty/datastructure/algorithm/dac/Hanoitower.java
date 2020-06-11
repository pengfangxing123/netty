package com.netty.datastructure.algorithm.dac;

/**
 * 分治算法
 * 汉诺塔问题
 * @author 86136
 */
public class Hanoitower {
    public static void main(String[] args) {
        mobile(3,'A','B','C');
    }

    /**
     * 移动
     * @param num 数量
     * @param a a柱子
     * @param b b柱子
     * @param c c柱子
     */
    public static void mobile(int num , char a ,char b ,char c){
        if(num == 1){
            //只有一个盘组时直接从A柱子移动到C柱子
            System.out.println("将第 "+num+" 个盘子从"+a+" -> "+c);
        }else {
            //两个柱子时，先将第一个从A移动到B
            mobile(num-1, a ,c ,b);

            //第二个从A移动到C
            System.out.println("将第 "+num+" 个盘子从"+a+" -> "+c);

          // 再将第一个从B移动到C
            mobile(num - 1, b, a, c);
        }

    }
}
