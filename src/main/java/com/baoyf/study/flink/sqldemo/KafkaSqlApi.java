package com.baoyf.study.flink.sqldemo;


import com.baoyf.study.flink.utils.CustomKafkaConnect;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.OldCsv;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.types.Row;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

public class KafkaSqlApi {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "wx12-test-hadoop001:6667");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "byf2345");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        FlinkKafkaConsumer customKafkaSource = CustomKafkaConnect.CustomKafkaSource("dwd_header", properties);
        customKafkaSource.setStartFromEarliest();

        DataStreamSource kafkaStreamSource = env.addSource(customKafkaSource);

//        Table table = tableEnv.fromDataStream(kafkaStreamSource, $("json"));
//
//        Table result = tableEnv.sqlQuery(
//                "SELECT json FROM " + table + " WHERE 1=1 ");

//        tableEnv.toAppendStream(table, Row.class).print("=====");


        tableEnv.createTemporaryView("kakfaSource", kafkaStreamSource, "json");
        Table table = tableEnv.sqlQuery("SELECT json FROM kakfaSource WHERE 1=1");

        tableEnv.toAppendStream(table, Row.class).print("====");

        final Schema schema = new Schema()
                .field("json", DataTypes.STRING());

        tableEnv.connect(new FileSystem().path("hdfs://uat/tmp/baoyf/file"))
                .withFormat(new OldCsv())
                .withSchema(schema)
                .createTemporaryTable("testFileSystemSink");

        tableEnv.executeSql(
                "INSERT INTO testFileSystemSink SELECT json FROM kakfaSource");

        env.execute("test table api");
    }
}
