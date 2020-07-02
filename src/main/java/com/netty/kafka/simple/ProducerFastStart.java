package com.netty.kafka.simple;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.aspectj.weaver.ast.Var;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * 代码清单1-1
 * Created by 朱小厮 on 2018/7/21.
 */
public class ProducerFastStart {
    public static final String brokerList = "120.79.201.200:9092";
    public static final String topic = "topic-demo";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers", brokerList);


        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello, Kafka!");
        try {
//            for (int i=0 ;i <10;i++){
//                producer.send(record);
//            }

            Future<RecordMetadata> send = producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println(metadata.partition() + ":" + metadata.offset());
                    }
                }
            });
            RecordMetadata metadata = send.get();
            System.out.println(metadata.partition() + ":" + metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }
}
