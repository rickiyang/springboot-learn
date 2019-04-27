package com.rickiyang.learn.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Arrays;
import java.util.Properties;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @Auther: rickiyang
 * @Date: 2019/4/27 11:37
 * @Description:
 */
public class KafkaConsumer implements Runnable {
    public void run() {
        Properties props = new Properties();
        props.put("group.id", "kevin_test");
        props.put("bootstrap.servers", "g1-ana-mq-01.dns.guazi.com:9092,g1-ana-mq-02.dns.guazi.com:9092");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);
        String topic = "test_kafka_topic";
        consumer.subscribe(Arrays.asList(topic));
        System.out.println("Consumer start");

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(100L);
            records.forEach((record) -> {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                System.out.println("delay : " + (System.currentTimeMillis() - Long.parseLong((String)record.value())));
            });
        }
    }
}
