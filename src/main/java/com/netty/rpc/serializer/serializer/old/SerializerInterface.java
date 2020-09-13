package com.netty.rpc.serializer.serializer.old;

/**
 * 序列化标准接口
 * @author ex-pengfx
 */
public interface SerializerInterface {
    /**
     * 序列化
     * @param <T>
     * @param obj
     * @return
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     * @param data
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T deSerialize(byte[] data,Class<T> tClass);
}
