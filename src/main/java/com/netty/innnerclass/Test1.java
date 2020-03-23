package com.netty.innnerclass;

/**
 * @author 86136
 */
public class Test1 {
    private String name="liutao";
    private static String age="27";
    private String tt="outTt";

    public class InTest1 {
        //非静态内部类不能拥有静态代码块
//        static {
//
//        }
        //非静态内部类不能声明静态方法
        //private static void getStr();
        private  String address1="sichuan";

        private String name="tt";
        public InTest1() {
            //内部类对象间接的共享了外部类对象的特性，包括静态变量，静态方法
            //System.out.println(name);
            //System.out.println(age);
            //test();
            //test2();
        }
        public void innerSay(){
            say();
        }

        public void difAge(){
            System.out.println(this.name);
            System.out.println(Test1.this.name);
        }
    }

    public static class InTest2 {
        private static String address2="sichuan";
        private  String age="tt";
        private String tt= "tt";
        public InTest2() {
            //内部类可以间接的访问外部类的静态私有特性
            System.out.println(age);
            //不能访问非静态私有特性
            //System.out.println(name);

        }

        public  void difAge(){
            System.out.println(age);
            System.out.println(Test1.age);
        }
    }


    public void test() {
        //外部类可以间接的访问内部类的静态私有属性
        System.out.println(InTest2.address2);
        InTest1 inTest1 = new InTest1();
        System.out.println(inTest1.address1);
        //非静态则要创建内部类对象
        InTest2 inTest2 = new InTest2();
        System.out.println(inTest2.tt);
    }

    public static void test2() {
        //外部类可以间接的访问内部类的静态私有属性
        System.out.println("6666");
    }

    public void say(){
        System.out.println("supSay");
    }

    public static void main(String args[]) {
        InTest2 inTest2 = new InTest2();
        //Test1 test1 = new Test1();
    }
}

class tt{

}
