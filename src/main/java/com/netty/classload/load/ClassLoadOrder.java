package com.netty.classload.load;

import org.aspectj.weaver.ast.Var;

/**
 * @author 86136
 */
public class ClassLoadOrder {
    public static void main(String[] args) {
        PersonA personA = new PersonA();

    }

}

class PersonA extends PersonB{
    static {
        System.out.println("PersonA class load");
    }
}

class PersonB{
    //引用类型不会被触发类加载
    public PersonC personC;
    static {
        System.out.println("PersonB class load");
    }
}

class PersonC{
    //引用类型不会被触发类加载
    public PersonD personD ;
    static {
        System.out.println("PersonC class load");
    }
}

class PersonD{
    static {
        System.out.println("PersonD class load");
    }
}
