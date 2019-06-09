package com.netty;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class Test {

    public static String compute(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    public static void main(String[] args) {
//        String compute = compute("genius88394688");
//        System.out.println(compute);
//
//        ByteBuffer.allocate(10);

        List<Map<String,Integer>> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            Map<String, Integer> map = new HashMap<>();
            if(i/2==0){
                map.put("name",1);
            }else{
                map.put("name",2);
            }
            list.add(map);
        }

//        Map<Object, List<Map<String, Object>>> name = list.stream().collect(Collectors.groupingBy(p -> p.get("name")));
//        System.out.println(name.toString());

        //break
//        Optional<Map<String, Object>> name = list.stream().filter(p -> {
//            if (p.get("name") != null) {
//                System.out.println("1111");
//                return true;
//            }else{
//                return false;
//            }
//        }).findFirst();
//        System.out.println(name.isPresent());

        //continue
//        list.forEach(p->{
//            if (p.get("name")==2)
//                return;
//            System.out.println(p.get("name"));
//        });

    }
}
