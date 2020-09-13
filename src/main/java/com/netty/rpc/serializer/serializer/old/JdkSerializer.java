package com.netty.rpc.serializer.serializer.old;

import java.io.*;

/**
 * jdk标准序列化
 * @author ex-pengfx
 */
public class JdkSerializer implements SerializerInterface {
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> tClass) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            return (T)inputStream.readObject();
        } catch ( Exception e) {
            e.printStackTrace();
        }finally {
            try {
                arrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
