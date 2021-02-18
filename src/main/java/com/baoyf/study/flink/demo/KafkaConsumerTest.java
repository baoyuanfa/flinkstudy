package com.baoyf.study.flink.demo;

import com.alibaba.fastjson.JSON;
import com.baoyf.study.flink.utils.CustomKafkaConnect;
import com.baoyf.study.flink.utils.DateParseUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

public class KafkaConsumerTest {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStateBackend((StateBackend) new RocksDBStateBackend("/tmp/baoyf/checkoutpoint", true));


        env.enableCheckpointing(10000);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "hadoop001:6667");
        properties.setProperty("group.id", "byf2345");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");


        FlinkKafkaConsumer kafkaConsumer = CustomKafkaConnect.CustomKafkaSource("dwd_header", properties);

//        Map<KafkaTopicPartition, Long> specificStartOffsets = new HashMap<>();
//        specificStartOffsets.put(new KafkaTopicPartition("dwd_header", 0), 1398L);
//        specificStartOffsets.put(new KafkaTopicPartition("bigdata_mobile50bang_logs", 1), 3L);
//        specificStartOffsets.put(new KafkaTopicPartition("bigdata_mobile50bang_logs", 2), 3L);

//        kafkaConsumer.setStartFromSpecificOffsets(specificStartOffsets);

//        kafkaConsumer.setStartFromEarliest();

//        Long fromTime = DateParseUtil.dateToTime("2021-02-12 22:01:17", "yyyy-MM-dd hh:mm:ss");
//        kafkaConsumer.setStartFromTimestamp(fromTime);

        DataStreamSource<String> stream = env.addSource(kafkaConsumer);

        SingleOutputStreamOperator<Object> server_time = stream.map(new MapFunction<String, Object>() {
            @Override
            public Object map(String s) throws Exception {

                Thread.sleep(3000);

                String serverTime = JSON.parseObject(s).getString("server_time");
                return DateParseUtil.timeToDate(Long.parseLong(serverTime) * 1000, "yyyy-MM-dd HH:mm:ss");

//                return JSON.parseObject(s).getString("server_time");
            }
        });

        server_time.setParallelism(1);

        server_time.print("kafkaConsumer: ").setParallelism(1);

//        String outputPath = "hdfs://uat/tmp/baoyf/flinkpath";
//
//        StreamingFileSink<String> sink = StreamingFileSink
//                .forRowFormat(new Path(outputPath), new SimpleStringEncoder<String>("UTF-8"))
//                .withRollingPolicy(
//                        DefaultRollingPolicy.builder()
//                                .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
//                                .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
//                                .withMaxPartSize(1024 * 1024 * 1024)
//                                .build())
//                .build();
//
//        stream.addSink(sink);


        env.execute("test kafka consumer");
    }
}
