package com.netty.kafka.comsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 *测试提交
 */
public class ConsumerStart {
    public static final String brokerList = "120.79.201.200:9092";
    public static final String topic = "topic-demo";
    public static final String groupId = "group.demo";

    public static void main(String[] args) {
//        Properties properties = new Properties();
//        properties.put("key.deserializer",
//                "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.put("value.deserializer",
//                "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.put("bootstrap.servers", brokerList);
//        properties.put("group.id", groupId);
//        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
//        properties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,ConsumerInterceptorDemo.class.getName());
//
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
//        consumer.subscribe(Collections.singletonList(topic));
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println(record.value()+"分区号："+record.partition()+" 偏移量："+record.offset());
//                //这种提交根据interceptor打印的信息可以知道，每次提交的都是postion的值
//                consumer.commitAsync();
//            }
//        }
    }
}
