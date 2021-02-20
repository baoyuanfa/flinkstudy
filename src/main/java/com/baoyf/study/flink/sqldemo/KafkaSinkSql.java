package com.baoyf.study.flink.sqldemo;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;
import org.apache.flink.table.runtime.operators.TableStreamOperator;
import org.apache.flink.types.Row;

public class KafkaSinkSql {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getCheckpointConfig().setCheckpointInterval(10000);
        StreamTableEnvironment tblEnv = StreamTableEnvironment.create(env);

        String catalogName="hive_catalog";
        String dataBase="dw_db";
        String hiveConfDir="src/main/resources";
        String hiveVersion="3.1.1";

        HiveCatalog hiveCatalog = new HiveCatalog(catalogName, dataBase, hiveConfDir, hiveVersion);

        tblEnv.registerCatalog(catalogName, hiveCatalog);
        tblEnv.useCatalog(catalogName);
        tblEnv.useDatabase(dataBase);

        tblEnv.executeSql("drop table if exists dw_header_kafkaSink");
        String kafkaSinkDDl=
                " create table dw_header_kafkaSink ( " +
                        " uid string comment '设备uid' " +
                        ",server_time string comment '上报时间戳' " +
                        ",ts TIMESTAMP(3) comment '时间时间戳' " +
                        ",WATERMARK FOR ts AS ts - INTERVAL '5' SECOND " +
                        ") comment 'kafkaSinkTable' with (" +
                        " 'connector' = 'kafka',\n" +
                        " 'topic' = 'dw_header_sink',\n" +
                        " 'properties.bootstrap.servers' = 'wx12-test-hadoop001:6667',\n" +
                        " 'properties.group.id' = 'byf2345',\n" +
                        " 'format' = 'json',\n" +
                        " 'scan.startup.mode' = 'earliest-offset',\n" +
                        " 'sink.partitioner'='round-robin'\n" +
                        ") ";

        tblEnv.executeSql(kafkaSinkDDl);

        String kakfaSinkSql=
                "        insert into dw_header_kafkaSink " +
                        "select uid, server_time,cast(FROM_UNIXTIME(cast(server_time as bigint)) as TIMESTAMP(3)) as ts from dw_header_test1";

        //cast(FROM_UNIXTIME(cast(server_time as bigint)) as TIMESTAMP(3))
        String kakfaSinkPrintSql = "select uid, server_time,cast(FROM_UNIXTIME(cast(server_time as bigint)) as TIMESTAMP(3)) as ts from dw_header_test1 ";
        Table table = tblEnv.sqlQuery(kakfaSinkPrintSql);

        tblEnv.toAppendStream(table, Row.class).print("=====");
        tblEnv.executeSql(kakfaSinkSql);

        env.execute("server_time cast to flink timestamp");
    }
}
