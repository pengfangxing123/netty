package com.netty.kafka.simple;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代码清单1-2
 * Created by 朱小厮 on 2018/7/21.
 */
public class ConsumerFastStart {
    public static final String brokerList = "120.79.201.200:9092";
    public static final String topic = "topic-demo";
    public static final String groupId = "group.demo2";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("bootstrap.servers", brokerList);
        properties.put("group.id", groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        //consumer.subscribe(Collections.singletonList(topic));
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        List<TopicPartition> collect = partitionInfos.stream().map(p -> new TopicPartition(p.topic(), p.partition()))
                .collect(Collectors.toList());
        consumer.assign(collect);
        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofSeconds(1000000000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value()+"分区号："+record.partition()+" 偏移量："+record.offset());
            }
        }
    }
}
