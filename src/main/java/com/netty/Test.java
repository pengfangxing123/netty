package com.netty;

import com.google.common.collect.Lists;
import io.netty.util.IllegalReferenceCountException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    static class Persion{
        private String name;
        private String age;
        private String sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Persion(String name, String age, String sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        public Persion() {
        }
    }

    public static void main(String[] args) throws UnknownHostException, ExecutionException, InterruptedException {
//        String compute = compute("genius88394688");
//        System.out.println(compute);
//
//        ByteBuffer.allocate(10);

//        List<Map<String,Integer>> list = new ArrayList<>();
//        for(int i=0;i<10;i++){
//            Map<String, Integer> map = new HashMap<>();
//            if(i/2==0){
//                map.put("name",1);
//            }else{
//                map.put("name",2);
//            }
//            list.add(map);
//        }
//
//        IntSummaryStatistics name = list.stream().collect(Collectors.summarizingInt(p -> p.get("name")));
//        double average = name.getAverage();
//        long count = name.getCount();
//        int max = name.getMax();
//        int min = name.getMin();
//        long sum = name.getSum();

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

//        List<Persion> list=Lists.newArrayList();
//        Persion xm = new Persion("xm", "19", "n"),
//            xmm=new Persion("xmm","199","n"),
//            xh = new Persion("xh", "19", "n"),
//            xhh=new Persion("xhh","19","n");
//        list.add(xm);
//        list.add(xmm);
//
//
////        List<Persion> list2=Lists.newArrayList();
////
////        list2.add(xh);
////        list2.add(xhh);
//        List<String> list2=Lists.newArrayList();
//        list2.add("19");
//        list2.add("18");
//
//        List<Persion> collect = list.stream().filter(p -> {
//            String age = p.getAge();
//            Optional<String> first = list2.stream().filter(o -> o.equals(age)).findFirst();
//            return first.isPresent();
//        }).collect(Collectors.toList());
//        System.out.println(collect.toString());



//
//        System.out.println(Inet4Address.getLocalHost().getHostAddress());
//        List<Integer>list=new ArrayList<>();
//        Integer minValue = list.stream().collect(Collectors.collectingAndThen(
//                Collectors.minBy(Comparator.comparingInt(a -> a)), p-> p.orElse(null))
//        );


//        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
//        Resource resource = resourceLoader.getResource("classpath:hhh.txt");
//        String filename = resource.getFilename();
//        System.out.println(filename);

        //testException();
        //testStack();
        Map<String, String> map=new HashMap<>();
        map.put("name","name");
        map.put("age","age");
        System.out.println(map);
    }


    public static void testException(){
        int rawCnt=2, decrement=1;

        int magic=99;
        if (rawCnt == 2 || rawCnt == 4 || (rawCnt & 1) == 0) {
            magic=rawCnt >>> 1;
        }
        // odd rawCnt => already deallocated
        throw new IllegalReferenceCountException(0, -decrement);
    }


    public static void testStack(){
        System.out.println(Arrays.toString(new Exception().getStackTrace()));
        System.out.println();
        System.out.println();
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        testStack2();

    }

    public static void testStack2(){
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        testStack3();
    }

    public static void testStack3(){
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}
