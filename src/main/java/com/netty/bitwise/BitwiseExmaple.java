package com.netty.bitwise;

/**
 * @author 86136
 */
public class BitwiseExmaple {
    public static void main(String[] args) {
        findNextPositivePowerOfTwo(16);
    }

    /**
     * 获取2^n，当符合2^n是返回原值
     * @param value
     * @return
     */
    public static int findNextPositivePowerOfTwo(final int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    /**
     * log2(pageSize)
     * @param pageSize
     * @return
     */
    public static int log2(int pageSize){
        return Integer.SIZE - 1 - Integer.numberOfLeadingZeros(pageSize);
    }
}
