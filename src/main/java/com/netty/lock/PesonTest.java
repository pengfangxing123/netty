package com.netty.lock;

/**
 * @author Administrator
 */
public class PesonTest {
    private String name;
    private volatile int age;

    public PesonTest(String name) {
        this.name = name;
    }

    public Men newMen(String totall){
        return new Men(totall);
    }


    class Men{
        private String totall;

        public Men(String totall) {
            this.totall = totall;
        }

        public void setNameMen(String name1){
            name=name1;
        }
    }

    public static void main(String[] args) {
        PesonTest xiaoming = new PesonTest("xiaoming");
        PesonTest.Men men = xiaoming.newMen("180");
        men.setNameMen("xiaohua");
        PesonTest.Men men2 = xiaoming.newMen("1800");
        men2.setNameMen("xiaohua2");
        System.out.println("");
    }
}
