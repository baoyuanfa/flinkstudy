package com.baoyf.study.flink.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParseUtil {

    // 时间戳转换为日期
    public static String timeToDate(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(timestamp));
    }

    // 日期转换为时间戳
    public static Long dateToTime(String date, String format) {
        Date parseDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            parseDate = sdf.parse(date);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        return parseDate.getTime();
    }

    public static void main(String[] args) {
/*        Long aLong = DateParseUtil.DateToTime("2021-02-09 00:17:07", "yyyy-MM-dd hh:mm:ss");
        System.out.println(aLong);*/

        //1612825975
        String date = DateParseUtil.timeToDate(1612825975000L, "yyyy-MM-dd hh:mm:ss");
        System.out.println(date);
    }
}
