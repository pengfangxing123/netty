package com.netty.spring.xml.customxmltag.others;

/**
 * DefaultNamespaceHandlerResolver#handlerMappings在debug的时候明明没调用却初始化了的原因分析
 * DefaultNamespaceHandlerResolver的toString()有调用getHandlerMappings()的方法,debug的时候多次调用了toString()
 * @author 86136
 */
public class MappingInit {
    /**
     * 验证Debug时，idea会开启一个线程调用对象的toString方法
     * tostring 还调用了多次
     */
    public static void main(String[] args) {

        WilltoStringInvoked will = new WilltoStringInvoked();

        System.out.println("如果在这里设置断点，则输出1");

        System.out.println(will.getValue());

        System.out.println("如果不设置断点，则输出0");

    }

    static class WilltoStringInvoked {
        private volatile int value = 0;

        private int setValue() {
            if (value == 0) {
                synchronized (this) {
                    if (value == 0) {
                        value = 1;
                    }
                }
            }
            return value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            System.out.println("6666");
            return "This value is:" + setValue();
        }
    }

}
