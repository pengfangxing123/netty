package com.netty.innnerclass;

import java.util.List;

/**
 * @author 86136
 */
public class Person <T extends Cat>{

    public static void sshow(List<? super Cat> list){

    }

    public static void sshow1(List<? extends Cat> list){

    }
}
