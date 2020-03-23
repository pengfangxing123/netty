package com.netty.innnerclass;

/**
 * @author 86136
 */
public class TestSub extends Test1{
    private String subName="subName";
    @Override
    public void say(){
        System.out.println("say");
        //子类不能访问内部类的私有特性
        //InTest1 inTest1 = new InTest1();
        //System.out.println(inTest1.address1);
    }
}
