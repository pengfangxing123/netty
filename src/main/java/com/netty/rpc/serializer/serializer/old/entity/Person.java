package com.netty.rpc.serializer.serializer.old.entity;

import lombok.Data;
import lombok.NonNull;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author ex-pengfx
 */
@Data
public class Person implements Serializable {
    @NonNull
    private int age;
    @NonNull
    private String name;
    /**
     * transient修饰默认不会序列化
     */
    @NonNull
    private transient String  address;
    /**
     * 序列化时也会序列化该引用对象，所有这个Dept也要实现序列化接口
     */
    @NonNull
    private Dept dept;

    /**
     * 用transient 关键词修改的字段可以用下面两个方法来实现序列化
     * @param out
     * @throws Exception
     */
    private void writeObject(ObjectOutputStream out) throws Exception{
        out.defaultWriteObject();
        out.writeUTF(this.address);
    }

    private void readObject(ObjectInputStream in) throws Exception{
        in.defaultReadObject();
        this.address=in.readUTF();
    }

    public Person(int age, String name, String address, Dept dept) {
        this.age = age;
        this.name = name;
        this.address = address;
        this.dept = dept;
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
