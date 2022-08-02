package com.springboot.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*日期处理类*/
public class DateUtils {
    
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    
    private DateUtils() {
    }
    
    /**
     * Title: getNowDate
     * </p>
     * Date: 2012-8-22 下午03:45:15
     * </p>
     * Description: 获取当前日期
     * </p>
     * @param
     * @return return String 返回yyyyMMdd格式日期
     * </p>
     * throws
     */
    public static String getNowDate() {
        return format("yyyyMMdd");
    }
    
    /**
     * Title: getNowDate
     * </p>
     * Date: 2012-8-22 下午03:45:32
     * </p>
     * Description: 获取当前日期
     * </p>
     * @param
     * @param pattern 日期格式
     * @param
     * @return return String 返回格式化后日期
     * </p>
     * throws
     */
    public static String getNowDate(String pattern) {
        return format(pattern);
    }
    
    /**
     * Title: format
     * </p>
     * Date: 2012-8-22 下午03:45:48
     * </p>
     * Description: 获取当前日期
     * </p>
     * @param
     * @param pattern 日期格式
     * @param
     * @return return String 返回格式化后日期
     * </p>
     * throws
     */
    public static String format(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }
    
    /**
     * Title: getPreMonth
     * </p>
     * Date: 2012-8-22 下午03:46:06
     * </p>
     * Description: 获得上个月的会计期
     * </p>
     * @param
     * @param pattern 日期格式
     * @param
     * @return return String 返回格式化后会计期
     * </p>
     * throws
     */
    public static String getPreMonth(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Date now = new Date();
        Calendar cd = Calendar.getInstance();
        cd.setTime(now);
        cd.add(Calendar.MONDAY, -1);
        Date time = cd.getTime();
        return df.format(time);
    }
    
    /**
     * 指定日期为星期几
     * @return
     */
    public static int dayForWeek(String date, String fomatStr) {
        SimpleDateFormat format = new SimpleDateFormat(fomatStr);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            logger.error("日期转为星期几失败说", e);
        }
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }
    
    /**
     * 日期转为String
     * @return
     */
    public static String toString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    
    /**
     * Title: format
     * </p>
     * Date: 2012-8-22 下午03:46:33
     * </p>
     * Description: 将日期类型转换为字符类型日期
     * </p>
     * @param
     * @param date 传入日期
     * @param
     * @return return String 返回yyyy-MM-dd格式字符串
     * </p>
     * throws
     */
    public static String format(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
    
    /**
     * Title: getNowDateTime
     * </p>
     * Date: 2012-8-22 下午03:46:49
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @return return Date 返回yyyy-MM-dd HH:mm:ss格式日期
     * </p>
     * throws
     */
    public static Date getNowDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return df.parse(getNowDate("yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        
        return null;
    }
    
    /**
     * Title: getDate
     * </p>
     * Date: 2012-8-22 下午03:47:03
     * </p>
     * Description: 获取当前日期
     * </p>
     * @param
     * @return return Date 返回yyyy-MM-dd格式日期
     * </p>
     * throws
     */
    public static Date getDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(getNowDate("yyyy-MM-dd"));
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        
        return null;
    }
    
    /**
     * Title: getDate
     * </p>
     * Date: 2012-8-22 下午03:47:17
     * </p>
     * Description: 字符串日期转Date类型日期
     * </p>
     * @param
     * @param date 输入日期
     * @param
     * @return return Date 返回yyyy-MM-dd格式日期
     * </p>
     * throws
     */
    public static Date getDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        
        return null;
    }
    
    /**
     * Title: getNowTime
     * </p>
     * Date: 2012-8-22 下午03:47:31
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @return return String 返回 HHmmss 格式字符串
     * </p>
     * throws
     */
    public static String getNowTime() {
        return format("HHmmss");
    }
    
    /**
     * Title: getNowTime
     * </p>
     * Date: 2012-8-22 下午03:47:43
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @param pattern 格式化类型
     * @param
     * @return return String 返回pattern格式化时间
     * </p>
     * throws
     */
    public static String getNowTime(String pattern) {
        return format(pattern);
    }
    
    /**
     * Title: getFullTime
     * </p>
     * Date: 2012-8-22 下午03:48:00
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @return return String 返回yyyyMMddHHmmssSSS格式化时间
     * </p>
     * throws
     */
    public static String getFullTime() {
        return format("yyyyMMddHHmmssSSS");
    }
    
    public static String getFullTimes() {
        return format("yyyyMMddHHmmss");
    }
    
    public static String getFullNowTime() {
        return format("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * Title: getFullTime
     * </p>
     * Date: 2012-8-22 下午03:48:10
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @param pattern
     * @param
     * @return return String 返回pattern格式化时间
     * </p>
     * throws
     */
    public static String getFullTime(String pattern) {
        return format(pattern);
    }
    
    /**
     * Title: parse
     * </p>
     * Date: 2012-8-22 下午03:48:24
     * </p>
     * Description: 将yyyy-MM-dd格式日期转换为yyyyMMdd格式日期
     * </p>
     * @param
     * @param orgi 传入yyyy-MM-dd格式日期
     * @param
     * @return return String 传入为空则返回空；传入日期不包括'-'字符返回空；
     * </p>
     * throws
     */
    public static String parse(String orgi) {
        if (StringUtils.isBlank(orgi)) {
            return StringUtils.EMPTY;
        }
        String dest = null;
        if (orgi.indexOf('-') < 0) {
            return orgi;
        }
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(orgi);
            df = new SimpleDateFormat("yyyyMMdd");
            dest = df.format(date);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return dest;
    }
    
    /**
     * Title: parse
     * </p>
     * Date: 2012-8-22 下午03:48:24
     * </p>
     * Description: 将yyyy-MM-dd格式日期转换为yyyyMMdd格式日期
     * </p>
     *
     *
     * 传入yyyy-MM-dd格式日期
     * @param
     * @return return String 传入为空则返回空；传入日期不包括'-'字符返回空；
     * </p>
     * throws
     */
    public static String getDateString(Date date) {
        try {
            if (date == null) {
                date = new Date();
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } catch (Exception e) {
            logger.error("日期格式化失败", e);
        }
        return null;
    }
    
    /**
     * Title: parse2
     * </p>
     * Date: 2012-8-22 下午03:48:40
     * </p>
     * Description: 将 yyyy年MM月dd日 格式日期转换为yyyy-MM-dd格式日期
     * </p>
     * @param
     * @param orgi 传入 yyyy年MM月dd日 格式日期
     * @param
     * @return return String String 返回yyyy-MM-dd格式字符串
     * </p>
     * throws
     */
    public static String parse2(String orgi) {
        if (StringUtils.isBlank(orgi)) {
            return StringUtils.EMPTY;
        }
        String dest = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = df.parse(orgi);
            df = new SimpleDateFormat("yyyy-MM-dd");
            dest = df.format(date);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return dest;
    }
    
    /**
     * Title: parseLong2Date
     * </p>
     * Date: 2012-8-22 下午03:48:57
     * </p>
     * Description: 将long类型日期转换为字符串类型日期
     * </p>
     * @param
     * @param date 传入long类型日期
     * @param
     * @return return String 返回yyyy-MM-dd格式的字符串类型日期
     * </p>
     * throws
     */
    public static String parseLong2Date(long date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date(date));
    }
    
    // 计算两个日期相差的天数
    public static int getBetweenDays(String dateB, String dateA)
            throws ParseException {
        long dayNumber = 0;
        long DAY = 24L * 60L * 60L * 1000L;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(dateA);
            Date d2 = df.parse(dateB);
            dayNumber = (d2.getTime() - d1.getTime()) / DAY;
        } catch (Exception e) {
            logger.error("日期计算失败", e);
        }
        return (int) dayNumber;
    }
    
    /**
     * Title: getBetweenMons
     * </p>
     * Date: 2012-8-22 下午03:49:16
     * </p>
     * Description: 计算两个日期相差的月数
     * </p>
     * @param
     * @param dateB string类型日期 格式yyyy-MM-dd
     * @param
     * @param dateA string类型日期 格式yyyy-MM-dd
     * @param
     * @param
     * @return
     * @throws Exception return int 两日期相差月数
     *                   </p>
     *                   throws
     */
    public static int getBetweenMons(String dateB, String dateA)
            throws Exception {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(getDate(dateB));
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(getDate(dateA));
        int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
                + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
        return c;
    }
    
    /**
     * Title: getBetweenYears
     * </p>
     * Date: 2012-8-22 下午03:49:43
     * </p>
     * Description: 计算两个日期相差的年数
     * </p>
     * @param
     * @param dateB string类型日期 格式yyyy-MM-dd
     * @param
     * @param dateA string类型日期 格式yyyy-MM-dd
     * @param
     * @param
     * @return
     * @throws Exception return float 返回两日期相差年数
     *                   </p>
     *                   throws
     */
    public static float getBetweenYears(String dateB, String dateA)
            throws Exception {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(getDate(dateB));
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(getDate(dateA));
        float c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
                + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
        return c / 12;
    }
    
    /**
     * Title: addMonths
     * </p>
     * Date: 2012-8-22 下午03:50:08
     * </p>
     * Description: 在一个日期上加指定的月数
     * </p>
     * @param
     * @param dateStr   日期 格式yyyy-MM-dd
     * @param
     * @param addMonths 加计月数
     * @param
     * @return return Date 返回yyyy-MM-dd格式日期
     * </p>
     * throws
     */
    public static Date addMonths(String dateStr, int addMonths) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonths);
        Date nowDate = calendar.getTime();
        return getDate(dateFormat.format(nowDate));
    }
    
    public static int getDayOfWeek(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 1) {
            w = 1;
        }
        
        return w;
    }
    
    /**
     * Title: addDays
     * </p>
     * Date: 2012-8-22 下午03:50:26
     * </p>
     * Description: 一个日期上加相应的天数
     * </p>
     * @param
     * @param dateStr 日期 格式yyyy-MM-dd
     * @param
     * @param days    加计天数 正数是加，负数是减
     * @param
     * @return return String 返回yyyy-MM-dd格式日期
     * </p>
     * throws
     */
    public static String addDays(String dateStr, int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        Date nowDate = calendar.getTime();
        return dateFormat.format(nowDate);
    }
    
    /**
     * Title: addMonthsEnd
     * </p>
     * Date: 2012-8-22 下午03:50:51
     * </p>
     * Description: 获取当日去年日期加上addMonths月数后的前一天
     * </p>
     * @param
     * @param dateStr   传入日期 格式yyyy-MM-dd
     * @param
     * @param addMonths 加计月数
     * @param
     * @return return Date 返回当日去年日期加上addMonths月数后的前一天 格式yyyy-MM-dd
     * </p>
     * throws
     */
    public static Date addMonthsEnd(String dateStr, int addMonths) {
        Date endMonth = addMonths(dateStr, addMonths);
        return getDate(addDays(format(endMonth), -1));
    }
    
    /**
     * Title: getCurDate
     * </p>
     * Date: 2012-8-22 下午03:51:11
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @return return String 返回当前时间 格式yyyy-MM-dd
     * </p>
     * throws
     */
    public static String getCurDate() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String result = f.format(date);
        return result;
    }
    
    /**
     * Title: getCurDate8
     * </p>
     * Date: 2012-8-22 下午03:51:24
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @return return String 返回当前时间 格式yyyyMMdd
     * </p>
     * throws
     */
    public static String getCurDate8() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String result = f.format(date);
        return result;
    }
    
    /**
     * Title: getCurDate
     * </p>
     * Date: 2012-8-22 下午03:51:34
     * </p>
     * Description: 获取当前时间
     * </p>
     * @param
     * @param farmat 时间格式
     * @param
     * @return return String 返回farmat格式时间
     * </p>
     * throws
     */
    public static String getCurDate(String farmat) {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat(farmat);
        String result = f.format(date);
        return result;
    }
    
    /**
     * Title: getCurMonthStart
     * </p>
     * Date: 2012-8-22 下午03:51:52
     * </p>
     * Description: 获取当月第一天（禁止使用，请使用ChangeDay.monthFirstDay方法）
     * </p>
     * @param
     * @return return String 返回当月第一天 格式yyyy-MM-dd
     * </p>
     * throws
     */
    public static String getCurMonthStart() {
        String result = getCurDate();
        result = result.substring(0, 7);
        result = result + "-01";
        return result;
    }
    
    /**
     * Title: getCurMonthStart8
     * </p>
     * Date: 2012-8-22 下午03:52:05
     * </p>
     * Description: 获取当月第一天（禁止使用，请使用ChangeDay.monthFirstDay方法）
     * </p>
     * @param
     * @return return String 返回当月第一天 格式yyyyMMdd
     * </p>
     * throws
     */
    public static String getCurMonthStart8() {
        String result = getCurDate8();
        result = result.substring(0, 6);
        result = result + "01";
        return result;
    }
    
    /**
     * Title: stringToDate
     * </p>
     * Date: 2012-8-22 下午03:52:17
     * </p>
     * Description: 字符串日期类型转换为日期类型
     * </p>
     * @param
     * @param str    字符串日期
     * @param
     * @param format 日期格式
     * @param
     * @return return Date 返回日期 格式format
     * </p>
     * throws
     */
    public static Date stringToDate(String str, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return date;
    }
    
    /**
     * Title: stringToDate10
     * </p>
     * Date: 2012-8-22 下午03:52:34
     * </p>
     * Description: 字符串类型日期转换为日期类型
     * </p>
     * @param
     * @param str 传入字符串 格式yyyy-MM-dd
     * @param
     * @return return Date 返回yyyy-MM-dd格式日期
     * </p>
     * throws
     */
    public static Date stringToDate10(String str) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return date;
    }
    
    /**
     * Title: stringToDate8
     * </p>
     * Date: 2012-8-22 下午03:52:49
     * </p>
     * Description: 字符串类型日期转换为日期类型
     * </p>
     * @param
     * @param str 传入字符串 格式yyyyMMdd
     * @param
     * @return return Date 返回yyyyMMdd格式日期
     * </p>
     * throws
     */
    public static Date stringToDate8(String str) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            logger.error("日期格式化失败", e);
        }
        return date;
    }
    
    /**
     * Title: dateToString10
     * </p>
     * Date: 2012-8-22 下午03:53:02
     * </p>
     * Description: 日期类型转换为字符串类型日期
     * </p>
     * @param
     * @param date 传入日期 格式yyyy-MM-dd
     * @param
     * @return return String 返回字符串日期 格式yyyy-MM-dd
     * </p>
     * throws
     */
    public static String dateToString10(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    
    public static String dateToString8(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }
    
    /**
     * Title: toString
     * </p>
     * Date: 2012-8-22 下午03:53:14
     * </p>
     * Description: 禁止使用
     * </p>
     * @param
     * @param date
     * @param
     * @return return String
     * </p>
     * throws
     */
    private static String toString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
    
    /**
     * Title: getOneMonthAgoDate
     * </p>
     * Date: 2012-8-22 下午03:53:25
     * </p>
     * Description: 获取当前日期前一月日期
     * </p>
     * @param
     * @return return Date 日期类型时间
     * </p>
     * throws
     */
    public static Date getOneMonthAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DATE, 1);
        
        return calendar.getTime();
    }
    
    /**
     * Title: getOneMonthAgoDateString
     * </p>
     * Date: 2012-8-22 下午03:53:36
     * </p>
     * Description: 获取当前日期前一月日期
     * </p>
     * @param
     * @return return String 字符串类型时间 格式yyyy-MM-dd
     * </p>
     * throws
     */
    public static String getOneMonthAgoDateString() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DATE, 1);
        
        return toString(calendar.getTime());
    }
    
    /**
     * Title: getd
     * </p>
     * Date: 2012-8-22 下午03:53:48
     * </p>
     * Description: 禁止使用
     * </p>
     * @param
     * @return return String
     * </p>
     * throws
     */
    public static String getd() {
        return Calendar.getInstance().get(Calendar.YEAR) + "-"
                + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        
    }
    
    /**
     * Title: getAgoServerDay
     * </p>
     * Date: 2012-8-22 下午03:53:55
     * </p>
     * Description: 获取当前日前七天的日期
     * </p>
     * @param
     * @return return String 返回字符串类型时间 格式yyyyMMdd
     * </p>
     * throws
     */
    public static String getAgoServerDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        String str = toString(calendar.getTime());
        return str.replaceAll("-", StringUtils.EMPTY);
    }
    
    /**
     * Title: addMonths
     * </p>
     * Date: 2012-8-22 下午03:54:06
     * </p>
     * Description: 输入开始日期 和 相加的月数 得到相加后日期
     * </p>
     * @param
     * @param inPrea    输入日期 格式yyyyMMdd
     * @param
     * @param addMonths 加计月数
     * @param
     * @return return String
     * </p>
     * throws
     */
    public static String addMonths(String inPrea, String addMonths) {
        Date date = stringToDate8(inPrea);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +Integer.parseInt(addMonths));
        String str = toString(calendar.getTime());
        return str.replaceAll("-", StringUtils.EMPTY);
    }
    
    /**
     * Title: getLastFriday
     * </p>
     * Date: 2012-8-22 下午03:54:27
     * </p>
     * Description: 获取输入日期的上周五时间
     * </p>
     * @param
     * @param day 输入日期 格式yyyyMMdd
     * @param
     * @return return String 返回字符串类型日期 格式yyyyMMdd
     * </p>
     * throws
     */
    public static String getLastFriday(String day) {
        Date date = stringToDate8(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int x = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DATE, -(x + 2));
        return toString(calendar.getTime());
    }
    
    /**
     * 比较两个日期的大小
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(Date DATE1, Date DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = DATE1;
            Date dt2 = DATE2;
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
                
            } else if (dt1.getTime() < dt2.getTime()) {
                
                return -1;
            } else {
                return 0;
            }
            
        } catch (Exception e) {
            
            logger.error("日期计算失败", e);
            
        }
        return 0;
        
    }
    
    /**
     * 取两个日期的最小值
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static String minDay(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return DATE2;
                
            } else {
                return DATE1;
            }
            
        } catch (Exception e) {
            
            logger.error("日期计算失败", e);
            
        }
        return null;
    }
	
	
	/*public static void main (String []args) throws ParseException {
//		int ts = getBetweenDays("2012-11-17", "2011-11-18");
		int ts = getBetweenDays("2012-12-28", "2012-06-29");
		System.out.println(ts);
		System.out.println("A0201201200043".substring(3,5));
	}*/
}
