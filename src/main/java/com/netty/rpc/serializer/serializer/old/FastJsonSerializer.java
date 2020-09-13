package com.netty.rpc.serializer.serializer.old;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author ex-pengfx
 */
public class FastJsonSerializer implements SerializerInterface{

    @Override
    public <T> byte[] serialize(T obj) {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat).getBytes();
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> tClass) {
        return (T) JSON.parseObject(new String(data), tClass);
    }
}
