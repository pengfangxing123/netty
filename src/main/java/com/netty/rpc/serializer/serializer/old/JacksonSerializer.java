package com.netty.rpc.serializer.serializer.old;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Date;

/**
 * @author ex-pengfx
 */
public class JacksonSerializer implements SerializerInterface{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        SimpleModule module = new SimpleModule("DateTimeModule", Version.unknownVersion());
        module.addSerializer(Date.class, new FDateJsonSerializer());
        module.addDeserializer(Date.class, new FDateJsonDeserializer());

        objectMapper.registerModule(module);

    }

    private static ObjectMapper getObjectMapperInstance() {
        return objectMapper;
    }


    @Override
    public <T> byte[] serialize(T obj) {
        if(obj==null)
            return new byte[0];
        try {
            //有 objectMapper.writeValueAsBytes(obj),为了统一；
            String json = objectMapper.writeValueAsString(obj);
            return json.getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> tClass) {
        
        String json = new String(data);
        try {
            //T t = objectMapper.readValue(data, tClass)
            return objectMapper.readValue(json,tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
