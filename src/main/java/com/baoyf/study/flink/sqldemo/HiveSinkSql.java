package com.baoyf.study.flink.sqldemo;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;
import org.apache.flink.types.Row;

public class HiveSinkSql {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        env.getCheckpointConfig().setCheckpointInterval(10000L);
        StreamTableEnvironment tblEnv = StreamTableEnvironment.create(env);

        String catalogName="hive_catalog";
        String dataBase="dw_db";
        String hiveConfDir="src/main/resources";
        String hiveVersion="3.1.1";

        tblEnv.registerCatalog(catalogName, new HiveCatalog(catalogName, dataBase, hiveConfDir, hiveVersion));
        tblEnv.useCatalog(catalogName);
        tblEnv.useDatabase(dataBase);

        //flink 建表, hive无法查询
//        tblEnv.executeSql("drop table if exists dw_header_hive_sink");
//        String hiveSinkDDl="" +
//                " create table dw_header_hive_sink ( " +
//                "  uid string comment '设备uid' " +
//                " ,server_time string comment '上报时间戳' \n" +
//                " ,p_dt string comment '日期分区' \n" +
//                " ,p_hours string comment '小时分区' \n" +
//                " ) comment 'hiveSinkTable' PARTITIONED BY (p_dt, p_hours) \n" +
//                " WITH (\n" +
//                "  'connector'='filesystem',\n" +
//                "  'path'='hdfs://uat/user/hive/warehouse/dw_db.db/dw_header_hive_sink',\n" +
//                "  'format'='orc',\n" +
//                "  'sink.partition-commit.trigger'='process-time',\n" +
//                "  'sink.partition-commit.delay'='0s',\n" +
//                "  'sink.partition-commit.policy.kind'='success-file',\n" +
//                "  'sink.rolling-policy.file-size'='1MB',\n" +
//                "  'sink.rolling-policy.rollover-interval'='30s',\n" +
//                "  'sink.rolling-policy.check-interval'='1min'\n" +
//                ")";
//        tblEnv.executeSql(hiveSinkDDl);

        //hive 建表语句：hive客户端执行！！！
//        String hiveSinkDDl="" +
//                " drop table if exists dw_db.dw_header_hive_sink;" +
//                " create table dw_db.dw_header_hive_sink ( " +
//                "  uid string comment '设备uid' " +
//                " ,server_time string comment '上报时间戳' \n" +
//                " ) comment 'hiveSinkTable' PARTITIONED BY (p_dt string comment '日期分区', p_hours string comment '小时分区') \n" +
//                " row format delimited \n" +
//                "  fields terminated by '\t' \n" +
//                "  collection items terminated by '\n' \n" +
//                "stored as orc tblproperties ( \n" +
//                "  'sink.partition-commit.trigger'='process-time',\n" +
//                "  'sink.partition-commit.delay'='0s',\n" +
//                "  'sink.partition-commit.policy.kind'='metastore,success-file',\n" +
//                "  'sink.rolling-policy.file-size'='1MB',\n" +
//                "  'sink.rolling-policy.rollover-interval'='1 min',\n" +
//                "  'sink.rolling-policy.check-interval'='1 min'\n" +
//                ")";

        String hiveSinkSql="" +
                " insert into dw_header_hive_sink " +
                " select uid, server_time, date_format(FROM_UNIXTIME(cast(server_time as bigint)), 'yyyy-MM-dd') as p_dt, date_format(FROM_UNIXTIME(cast(server_time as bigint)), 'HH') as p_hours from dw_header_kafkaSink";

        String hiveSinkPrintSql="" +
                " select uid, server_time, date_format(FROM_UNIXTIME(cast(server_time as bigint)), 'yyyy-MM-dd') as p_dt, date_format(FROM_UNIXTIME(cast(server_time as bigint)), 'HH') as p_hours  from dw_header_kafkaSink";
//        String hiveSinkPrintSql="select * from dw_header_hive_sink";
        Table table = tblEnv.sqlQuery(hiveSinkPrintSql);
        tblEnv.toAppendStream(table, Row.class).print("======================");

        tblEnv.executeSql(hiveSinkSql);
        env.execute("hive sink sql");
    }
}
