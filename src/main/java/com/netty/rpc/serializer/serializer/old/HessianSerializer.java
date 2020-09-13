package com.netty.rpc.serializer.serializer.old;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description: hessian序列化
 * @Copyright: Copyright(C) 2019
 * @author: ex-pengfx
 * @version: 1.0
 * @date: 2019/11/4 10:59
 */
public class HessianSerializer implements SerializerInterface{

    @Override
    public <T> byte[] serialize(T obj) {
        if(obj==null){
            throw new NullPointerException();
        }
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){

            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> tClass) {
        if(data==null || data.length <1){
            throw new NullPointerException();
        }

        try (ByteArrayInputStream is = new ByteArrayInputStream(data)){

            HessianInput io = new HessianInput(is);
            return (T)io.readObject();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
