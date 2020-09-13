package com.netty.rpc.serializer.serializer.old;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author ex-pengfx
 */
public class XmlSerializer implements SerializerInterface{
    private static final XStream xStream =new XStream(new DomDriver());

    @Override
    public <T> byte[] serialize(T obj) {
        String xml = xStream.toXML(obj);
        return xml.getBytes();
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> tClass) {
        String xml = new String(data);
        return (T) xStream.fromXML(xml);
    }
}
