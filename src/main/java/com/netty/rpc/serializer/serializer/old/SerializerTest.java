package com.netty.rpc.serializer.serializer.old;

import com.netty.rpc.serializer.serializer.old.entity.Dept;
import com.netty.rpc.serializer.serializer.old.entity.Person;

/**
 * @author ex-pengfx
 */
public class SerializerTest {
    public static void main(String[] args) {
        Person persion = new Person(10, "小明", "车公庙",new Dept("10086","开发部"));
        System.out.println(persion.toString());

        SerializerInterface serializer;
        //serializer= new JdkSerializer();
        //serializer=new XmlSerializer();
        serializer=new JacksonSerializer();
        byte[] data = serializer.serialize(persion);
        Person dePersion = serializer.deSerialize(data, Person.class);
        System.out.println(dePersion.toString());
    }
}
