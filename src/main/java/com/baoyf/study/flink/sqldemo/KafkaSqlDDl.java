package com.baoyf.study.flink.sqldemo;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;
import org.apache.flink.types.Row;

public class KafkaSqlDDl {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tblEnv = StreamTableEnvironment.create(env, settings);

        String catalogName="hive_catalog";
        String dataBase="dw_db";
        String hiveConfDir="src/main/resources";
        String hiveVersion="3.1.1";

        HiveCatalog hiveCatalog = new HiveCatalog(catalogName, dataBase, hiveConfDir, hiveVersion);
        tblEnv.registerCatalog("hive_catalog", hiveCatalog);
        tblEnv.useCatalog("hive_catalog");
        tblEnv.useDatabase("dw_db");

        tblEnv.executeSql("drop table if exists dw_header_test1");

        String headerDDl=
                " create table dw_header_test1 (\n" +
                "  uid string comment '设备uid'\n" +
                " ,server_time String comment 'json字符串'\n" +
                " ) comment 'header表结构' " +
                " with (\n" +
                " 'connector'='kafka',\n" +
                " 'topic'='dwd_header',\n" +
                " 'properties.bootstrap.servers'='wx12-test-hadoop001:6667',\n" +
                " 'properties.group.id'='byf2345',\n" +
                " 'format'='json',\n" +
                " 'scan.startup.mode'='earliest-offset'\n" +
                " ) ";

        tblEnv.executeSql(headerDDl);
        String querySql = "select uid, server_time from dw_header_test1";
        Table table = tblEnv.sqlQuery(querySql);
        tblEnv.toAppendStream(table, Row.class).print("======");

        env.execute("test kafka table");
    }
}
