package com.baoyf.study.flink.demo;

import org.apache.log4j.Logger;

public class TestLog4j2Kafka {
	 private static Logger logger = Logger.getLogger(TestLog4j2Kafka.class);
	 
	 public static void main(String[] args) throws InterruptedException {
		 for(int i = 0;i <= 10; i++) {
			 logger.info("This is Message [" + i + "] from log4j producer .. ");
			 Thread.sleep(10);
		 }
	 }
}