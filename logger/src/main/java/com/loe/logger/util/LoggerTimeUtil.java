package com.loe.logger.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoggerTimeUtil
{
    public static final int DAY_MILLIS = 86400000;
    public static final SimpleDateFormat hms = new SimpleDateFormat("h:mm");
    private static final SimpleDateFormat mdhms = new SimpleDateFormat("M月d日 h:mm");
    private static final SimpleDateFormat ymdhms = new SimpleDateFormat("y年M月d日 h:mm");

    public static String simpleFormat(long t)
    {
        return ymdhms.format(new Date(t));
    }

    /**
     * 时间格式化
     */
    public static String dynamicTimeFormat(long old)
    {
        long now = System.currentTimeMillis();
        long dt = now - old;
        if (dt < 0)
        {
            return "error";
        }
        Calendar calendar0 = Calendar.getInstance();
        calendar0.setTimeInMillis(old);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(now);
        int y0 = calendar0.get(Calendar.YEAR);
        int d0 = calendar0.get(Calendar.DAY_OF_YEAR);
        int y1 = calendar1.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);

        if (y0 == y1)
        {
            // 如果1天内
            if (d0 == d1)
            {
                // 如果是1小时内
                if (dt < 3600000)
                {
                    // 如果是1分钟内
                    if (dt < 60000)
                    {
                        return dt / 1000 + " 秒前";
                    }
                    return dt / 60000 + " 分钟前";
                }
                return "今天 " + hms.format(new Date(old));
            } else
            {
                if (d0 == d1 - 1)
                {
                    return "昨天 " + hms.format(new Date(old));
                }
            }
        } else
        {
            if (d1 == 1 && y0 == y1 - 1)
            {
                calendar1.add(Calendar.DATE, -1);
                // 为昨天
                if (calendar1.get(Calendar.DAY_OF_YEAR) == d0)
                {
                    return "昨天 " + hms.format(new Date(old));
                }
            }else
            {
                return ymdhms.format(new Date(old));
            }
        }
        return mdhms.format(new Date(old));
    }

    /**
     * 时间格式化
     */
    public static String timeFormat(long old)
    {
        return ymdhms.format(new Date(old));
    }

    /**
     * 获取0点的毫秒数
     */
    public static long get0Millis(long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取今天0点的毫秒数
     */
    public static long getTodayMillis()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取有多少天
     */
    public static int getDateNum(int year, int month)
    {
        if(month == 4 || month == 6 || month == 9 || month == 11)
        {
            return 30;
        }
        if(month == 2)
        {
            if (year % 100 == 0 && year % 400 != 0 || year % 4 != 0)
            {
                return 28;
            }else
            {
                return 29;
            }
        }
        return 31;
    }

    /**
     * 获取当前年份
     */
    public static int getNowYear()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     */
    public static int getNowMonth()
    {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * 获取当前日期
     */
    public static int getNowDate()
    {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 获取当前小时
     */
    public static int getNowHour()
    {
        return Calendar.getInstance().get(Calendar.HOUR);
    }

    /**
     * 获取当前分钟
     */
    public static int getNowMinute()
    {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒数
     */
    public static int getNowSecond()
    {
        return Calendar.getInstance().get(Calendar.SECOND);
    }
}

