package com.netty.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fangxing.peng
 */
public class NullConvertUtil {

    /**
     * null 转换
     * @param arg
     * @param tClass
     * @param <T>
     * @return
     * @throws UnsupportedOperationException
     */
    public static <T> T nullConvert(T arg , Class<T> tClass) throws UnsupportedOperationException {
        if(tClass.equals(Double.class)||tClass.equals(Integer.class)||tClass.equals(Float.class)){
            try {
                return arg == null? (T) tClass.getConstructor(String.class).newInstance("0") : arg;
            } catch (Exception e) {

            }
        }
        if(tClass.equals(String.class)){
            return arg == null? (T) new String("--") : arg;
        }

        throw new UnsupportedOperationException("unsupported type: " + tClass.getName() );
    };

    /**
     * obj转换 忽略null
     * @param arg
     * @param tClass
     * @param <T>
     * @return
     * @throws UnsupportedOperationException
     */
    public static <T> T objConvert(Object arg , Class<T> tClass) throws UnsupportedOperationException {
        if(tClass.equals(Double.class)||tClass.equals(Integer.class)
                ||tClass.equals(Float.class)||tClass.equals(Long.class)){
            try {
                return arg == null? null:(T) tClass.getConstructor(String.class).newInstance(String.valueOf(arg));
            } catch (Exception e) {

            }
        }
        if(tClass.equals(String.class)){
            return arg == null? null: (T) String.valueOf(arg);
        }

        throw new UnsupportedOperationException("unsupported type: " + tClass.getName()+" and arg: "+ arg);
    };


    public static void main(String[] args) throws Exception {
        List<PersonT<List<String>>> list = Lists.newArrayList();
        PersonT<List<String>> stringPersonT = new PersonT<List<String>>("xiaoming", new ArrayList<>());
        list.add(stringPersonT);
        list.forEach(p -> {
            System.out.println(p.toString());
        });
//        Map<String,Object> map=Maps.newHashMap();
//        map.put("int",222);
//        map.put("doub",222.22);
//        map.put("str","jhhhh");
//        map.put("null",null);
//        System.out.println(NullConvertUtil.objConvert(map.get("int"),Integer.class));
//        System.out.println(NullConvertUtil.objConvert(map.get("doub"),Double.class));
//        System.out.println(NullConvertUtil.objConvert(map.get("str"),String.class));
//        System.out.println(NullConvertUtil.objConvert(map.get("null"),String.class));
    }
}
class PersonT<T>{
    private String name;
    private T  other;

    public PersonT(String name, T other) {
        this.name = name;
        this.other = other;
    }

    @Override
    public String toString() {
        return "PersonT{" +
                "name='" + name + '\'' +
                ", other=" + other +
                '}';
    }
}