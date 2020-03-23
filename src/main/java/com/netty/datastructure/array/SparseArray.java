package com.netty.datastructure.array;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 稀疏数组
 * @author 86136
 */
public class SparseArray {

    public static void main(String[] args) {
        int[][] chesArr = new int[11][11];
        chesArr[1][2]=1;
        chesArr[2][4]=2;

        System.out.println("原数组为：");
        for(int [] row :chesArr){
            for(int data :row){
                System.out.print(data + "\t");
            }
            System.out.println();
        }

        int sum = 0;
        Map<Integer, Integer> map= Maps.newHashMap();
        for(int i =0;i < 11 ;i++){
            for(int j=0;j<11;j++){
                if(chesArr[i][j]!=0){
                    map.put(i,j);
                    sum++;
                }
            }
        }

        int[][] sparseArr =new int[sum+1][3];
        sparseArr[0][0]=11;
        sparseArr[0][1]=11;
        sparseArr[0][2]=sum;
        AtomicInteger i= new AtomicInteger(1);
        map.forEach((key,val)->{
            sparseArr[i.get()][0]=key;
            sparseArr[i.get()][1]=val;
            sparseArr[i.get()][2]=chesArr[key][val];
            i.getAndIncrement();
        });

        System.out.println("稀疏数组为：");
        for(int [] row :sparseArr){
            for(int data :row){
                System.out.print(data + "\t");
            }
            System.out.println();
        }


        int[][] convertArr = new int[sparseArr[0][0]][sparseArr[0][1]];
        for(int p =1 ;p<sparseArr.length;p++){
            convertArr[sparseArr[p][0]][sparseArr[p][1]]=sparseArr[p][2];
        }

        System.out.println("原始数组转换为：");
        for(int [] row :convertArr){
            for(int data :row){
                System.out.print(data + "\t");
            }
            System.out.println();
        }
    }
}
