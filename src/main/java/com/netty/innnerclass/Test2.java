package com.netty.innnerclass;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * @author 86136
 */
public class Test2 {
    /**
     * 创建对象的语法是new 外部类.内部类()
     */
    private Test1.InTest2 dd =new Test1.InTest2();

    /**
     * 创建对象的语法是new 外部类().new 内部类()
     */
    private Test1.InTest1 tt=new Test1().new InTest1();

    private TestSub.InTest1 ss=new TestSub().new InTest1();
    public static void main(String[] args) throws Exception{
        TestSub.InTest1 sbu=new TestSub().new InTest1();
        sbu.innerSay();
        Test1.InTest1 sup=new Test1().new InTest1();
        sup.innerSay();


        ArrayList<String>[] sz=new ArrayList[5];

        Class<Test2> clz = Test2.class;
        Test2 test2 = clz.newInstance();

        Class clz2 = Test2.class;
        Object o = clz2.newInstance();

        Class<Test2> clz3 = Test2.class;
        Constructor<Test2> constructor = clz3.getConstructor();
        Test2 test21 = constructor.newInstance();

    }
}
