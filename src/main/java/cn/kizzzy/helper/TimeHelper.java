package cn.kizzzy.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Supplier;

public class TimeHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(TimeHelper.class);
    
    private static long TIME_OFFSET = 0;
    private static final String webUrl = "http://www.ntsc.ac.cn"; // 中国科学院国家授时中心
    private static final String[] weekDays = {
        "星期日",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六"
    };
    
    public static long currentTimeMillis() {
        return TIME_OFFSET + System.currentTimeMillis();
    }
    
    public static Date currentDate() {
        return new Date(currentTimeMillis());
    }
    
    public static long getWebTime() throws IOException {
        URL url = new URL(webUrl);
        URLConnection conn = url.openConnection();
        conn.connect();
        return conn.getDate();
    }
    
    public static Date getWebDate() throws IOException {
        return new Date(getWebTime());
    }
    
    public static String format(Date date) {
        return format(date.getTime());
    }
    
    public static String format(long date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    public static String format(Date date, String pattern) {
        return format(date.getTime());
    }
    
    public static String format(long date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    public static Date parse(String str) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
    }
    
    public static String dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }
    
    public static String dayOfWeek2(Date date) {
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }
    
    public static Date dateAfter(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }
    
    public static Date todayBegin() {
        return dayBegin(currentDate());
    }
    
    public static Date todayFinal() {
        return dayFinal(currentDate());
    }
    
    public static Date dayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date dayFinal(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    public static int millisOfSecond() {
        return 1000;
    }
    
    public static int millisOfSecond(int n) {
        return millisOfSecond() * n;
    }
    
    public static int millisOfMinute() {
        return millisOfSecond(60);
    }
    
    public static int millisOfMinute(int n) {
        return millisOfMinute() * n;
    }
    
    public static int millisOfHour() {
        return millisOfMinute(60);
    }
    
    public static int millisOfHour(int n) {
        return millisOfHour() * n;
    }
    
    public static int millisOfDay() {
        return millisOfHour(24);
    }
    
    public static int millisOfDay(int n) {
        return millisOfDay() * n;
    }
    
    public static void setLocalTime(String strDate, String strTime) throws ParseException {
        setLocalTime(parse(strDate + " " + strTime));
    }
    
    public static void setLocalTime(Date date) {
        if (date != null) {
            TIME_OFFSET = date.getTime() - System.currentTimeMillis();
        }
    }
    
    /**
     * 比较2个时间是否是同一天
     */
    public static boolean compareDay(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        calendar2.setTimeInMillis(date2);
        return compareDay(calendar1, calendar2);
    }
    
    /**
     * 比较2个时间是否是同一天
     */
    public static boolean compareDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        return compareDay(calendar1, calendar2);
    }
    
    /**
     * 比较2个时间是否是同一天
     */
    public static boolean compareDay(Calendar calendar1, Calendar calendar2) {
        if (calendar1 == null || calendar2 == null) {
            return false;
        }
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }
    
    public static <T> T execute(String msg, Supplier<T> callback) {
        long time = System.currentTimeMillis();
        T t = callback.get();
        logger.debug(String.format("%s, cost %d ms", msg, System.currentTimeMillis() - time));
        return t;
    }
    
    public static void execute(String msg, Runnable runnable) {
        execute(null, msg, runnable);
    }
    
    public static void execute(String startMsg, String msg, Runnable runnable) {
        if (startMsg != null) {
            logger.debug(startMsg);
        }
        long time = System.currentTimeMillis();
        runnable.run();
        logger.debug(String.format("%s, cost %d ms", msg, System.currentTimeMillis() - time));
    }
}
