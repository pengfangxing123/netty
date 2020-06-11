package com.netty.designpatterns.strategy;

/**
 * @author 86136
 */
public class strategyMain {
    public static void main(String[] args) {
        int i=0;
        int j=0;
        retry:
        for(;;j++){
            System.out.println("j start。。。");
            for(;;i++){
                if(i==10){
                    break retry;
                }
                if(i==3||i==5||i==7){
                    i++;
                    continue retry;
                }

            }
        }
        System.out.println(i);
        System.out.println(j);
    }
}

