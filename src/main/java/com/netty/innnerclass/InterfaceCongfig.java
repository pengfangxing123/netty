package com.netty.innnerclass;

/**
 * 接口的内部类，会自动转为 public static 静态类
 * @author 86136
 */
public interface InterfaceCongfig {

    void todoSomething();

    class InterfaceCongfig1 implements InterfaceCongfig{

        @Override
        public void todoSomething() {
            //to do something
        }
    }

    class InterfaceCongfig2 implements InterfaceCongfig{

        @Override
        public void todoSomething() {
            //to do something
        }
    }

    public static void main(String[] args) {
        InterfaceCongfig interfaceCongfig = new InterfaceCongfig1();
        interfaceCongfig.todoSomething();
    }
}
