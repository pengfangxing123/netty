package com.netty.datastructure.algorithm.kmp;

import java.io.Serializable;
import java.util.Arrays;

/**
 * KMP算法
 * @author 86136
 */
public class KMPAlgorithm {

    public static void main(String[] args) {
        int[] table = kmpNext2("abacabaddabacabaad");
        System.out.println(Arrays.toString(table));

    }

    /**
     * 获取到一个字符串(子串) 的部分匹配值表
     * next表中，当前位存放当前位的前缀最大匹配长度
     * @param dest
     * @return
     */
    public static  int[] kmpNext(String dest) {
        //创建一个next 数组保存部分匹配值
        int[] next = new int[dest.length()];

        //如果字符串是长度为1 部分匹配值就是0
        next[0] = 0;
        for(int i = 1, j = 0; i < dest.length(); i++) {
            //当dest.charAt(i) != dest.charAt(j) ，我们需要从next[j-1]获取新的j
            //直到我们发现 有  dest.charAt(i) == dest.charAt(j)成立才退出
            //这时kmp算法的核心点
            while(j > 0 && dest.charAt(i) != dest.charAt(j)) {
                j = next[j-1];
            }

            //当dest.charAt(i) == dest.charAt(j) 满足时，部分匹配值就是+1
            if(dest.charAt(i) == dest.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

    //public void match(String ds ,String ps)

    /**
     * 获取next数组
     * next表中，当前位存放上一位的前缀最大匹配长度
     * @param dest
     * @return
     */
    public static int[] kmpNext2(String dest){
        int[] next=new int[dest.length()];

        //i代表后缀的位置，j代表前缀的位置
        int i=0,j=-1;

        //第一位的前一位啥都没有所以直接设置为-1
        next[0]=j;
        while(i<dest.length()-1){
            //j==-1也就是i=0时初始时，只有一个元素，所以有next[0+1]=-1++，起始就时next[1]=0
            //当T[i]=T[j]时，记录当前最大前缀匹配长度，放到i+1
            if(j==-1 || dest.charAt(i)==dest.charAt(j)){
                next[++i]=++j;
            }else{
                //当不匹配时，需要重新将j的位置重置
                //这个时候i之前T[i]!=T[j]，所以要重置j的位置，
                //可知i之前的元素部分是和前缀相同的，(i-j))~(i-1)这个j个元素是和0~(j-1)的元素是相等的，
                //因为T[i]!=T[j]，又((i-1)-j))~(i-1)这个j个元素是和0~(j-1)的元素是相等，
                //0   1   2   3   4   5   6   7   8
                //a   b   a   c   a   b   a   b   d
                //-1  0   0   1   0   1   2   3   2

                //再来个多次调用j=next[j]的，如下图
                //i=16,j=7 这个时候T[16]!=T[7]，所以说明T[9]~T[16]不能构成最大后缀长度，所以要求串T[10]~T[16]中最大长度
                //T[16]是比较位，现在要获取T[10]~T[15]现有的最大
                //0   1   2   3   4   5   6     7     8     9   10  11  12  13  14  15    16    17
                //a   b   a   c   a   b   a     d     d     a   b   a   c   a   b   a     a     d
                //-1  0   0   1   0   1   2     3     0     0   1   2   3   4   5   6     7     1
                //这个时候

                j=next[j];
            }
        }
        return next;
    }
}
