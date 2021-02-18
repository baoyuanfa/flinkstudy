package com.baoyf.study.flink.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class CustomKafkaConnect {

    public static FlinkKafkaConsumer CustomKafkaSource(String topic, Properties prop) {
        return new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), prop);
    }
}
