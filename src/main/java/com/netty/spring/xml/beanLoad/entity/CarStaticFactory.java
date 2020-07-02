package com.netty.spring.xml.beanLoad.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 86136
 */
public class CarStaticFactory {
    private static Map<Integer, Car> map = new HashMap<Integer,Car>();

    static{
        map.put(1, new Car(1,"Honda",300000));
        map.put(2, new Car(2,"Audi",440000));
        map.put(3, new Car(3,"BMW",540000));
    }

    public static Car getCar(int id){
        return map.get(id);
    }
}
