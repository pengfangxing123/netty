package com.netty.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author 86136
 */
public class SubTest extends SupTest{
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void main(String[] args) {
        SubTest subTest = new SubTest();
        subTest.setAddress("111");

        String s = JSON.toJSONString(subTest);
        System.out.println(s);
        SupTest supTest = JSON.parseObject(s, SupTest.class);
        System.out.println(JSON.toJSONString(supTest));

    }
}
