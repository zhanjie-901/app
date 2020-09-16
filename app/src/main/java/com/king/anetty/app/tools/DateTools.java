package com.king.anetty.app.tools;

import java.util.Calendar;

/**
 * @author 高金磊
 * @version 1.0
 * @date 2019/12/15 11:44
 * @项目名 android_last
 */
public class DateTools {
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    //禁止外部直接初始化以免拿到老版本的日期对象
    private DateTools() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }

    private Calendar getCalendar() {
        return calendar;
    }
    public static String getDate(){
        //保证每次获取的是最新的
        DateTools date=new DateTools();
        return ""+date.getMonth()+"-"+date.day+"  "+date.getHour()+":"+date.getMinute();
    }
    private int getYear() {
        return year;
    }

    private int getMonth() {
        return month;
    }

    private int getDay() {
        return day;
    }

    private int getHour() {
        return hour;
    }

    private int getMinute() {
        return minute;
    }

    private int getSecond() {
        return second;
    }
}
