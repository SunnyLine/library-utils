package com.library.util.common;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * X-Util<br>
 * describe ：时间工具类
 *
 * @author xugang
 * @date 2020/1/9
 */
public class DateUtil {
    public static final String PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YMDHMS = "yyyyMMddHHmmss";
    public static final String PATTERN_YMD = "yyyy-MM-dd";
    public static final String PATTERN_MD = "MM-dd";
    public static final String PATTERN_HMS = "HH:mm:ss";
    public static final String PATTERN_HM = "HH:mm";
    public static final String PATTERN_MD_HMS = "MM-dd HH:mm:ss";
    public static final String PATTERN_MD_HM = "MM-dd HH:mm";

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;
    public static final long ONE_HOUR = 60 * 60 * 1000;
    public static final long ONE_MIN = 60 * 1000;
    public static final long ONE_SECOND = 1000;
    public static final long EIGHTY_PERCENT_SECOND = 800;
    public static final long SIXTY_PERCENT_SECOND = 600;
    public static final long FIFTY_PERCENT_SECOND = 500;
    public static final long FORTY_PERCENT_SECOND = 400;
    public static final long TWENTY_PERCENT_SECOND = 200;
    public static final long TEN_PERCENT_SECOND = 100;


    /**
     * 东八区
     */
    public static final String GMT_8 = "GMT+08:00";

    public static String format2GMT(long timeStamp, String formatStr) {
        return format2GMT(timeStamp, formatStr, null);
    }

    public static String format2GMT(Date date, String formatStr) {
        return format2GMT(date, formatStr, null);
    }

    @SuppressLint("SimpleDateFormat")
    public static String format2GMT(Date date, String formatStr, String timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        if (!TextUtils.isEmpty(timeZone)) {
            format.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return format.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String format2GMT(long timeStamp, String formatStr, String timeZone) {
        return format2GMT(new Date(timeStamp), formatStr, timeZone);
    }

    public static long format2UTC(String date, String formatStr) {
        return format2UTC(date, formatStr, null);
    }

    @SuppressLint("SimpleDateFormat")
    public static long format2UTC(String date, String formatStr, String timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        if (!TextUtils.isEmpty(timeZone)) {
            format.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 获取当前手机设置时区
     *
     * @return
     */
    public static String getCurTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    /**
     * 判读是否是闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }


    /**
     * 是否是同一天
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time1, long time2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        cal2.setTimeInMillis(time2);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    /**
     * 计算两个时间戳之间间隔的自然日
     */
    public static int calcDiffNaturalDay(long time1, long time2) {
        return calcDiffNaturalDay(time1, time2, GMT_8);
    }

    /**
     * 计算两个时间戳之间间隔的自然日
     */
    public static int calcDiffNaturalDay(long time1, long time2, String timeZone) {
        String date1 = format2GMT(time1, PATTERN_YMD, timeZone);
        String date2 = format2GMT(time2, PATTERN_YMD, timeZone);
        BigDecimal bigDecimal1 = new BigDecimal(format2UTC(date1, PATTERN_YMD, timeZone));
        BigDecimal bigDecimal2 = new BigDecimal(format2UTC(date2, PATTERN_YMD, timeZone));
        BigDecimal oneDay = new BigDecimal(ONE_DAY);
        int diff = bigDecimal1.subtract(bigDecimal2).divide(oneDay, 0, ROUND_DOWN).intValue();
        return diff;
    }

    /**
     * 比较两个时间大小
     *
     * @return 1 即 date1 > date2;-1 即 date1 < date2 ; 0 即 date1 == date2
     */
    public static int compareDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal1.after(date2)) {
            return 1;
        }
        if (cal1.before(date2)) {
            return -1;
        }
        return 0;
    }
}
