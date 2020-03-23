package com.netty.innnerclass;

import java.lang.reflect.Array;

/**
 * @author 86136
 */
public class TestArray <T>{
    private T[] array;

    public TestArray( Class<T> clz,int length) {
        this.array = (T[]) Array.newInstance(clz,length);
    }
}
