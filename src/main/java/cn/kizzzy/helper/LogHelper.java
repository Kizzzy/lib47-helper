package cn.kizzzy.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
    private static final String THIS_NAME = LogHelper.class.getName();
    private static final Logger LOGGER;// = LoggerFactory.getLogger(LogHelper.class);
    
    static {
        System.setProperty("log4j.configuratorClass", "cn.kizzzy.helper.Log4jConfigurator");
        LOGGER = LoggerFactory.getLogger(LogHelper.class);
    }
    
    public static void debugWithStack(Object msg) {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        if (traces.length < 3) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < traces.length; ++i) {
            builder.append("\t");
            builder.append(traces[i]);
            if (i == 2) {
                builder.append(msg);
            }
            builder.append("\r\n");
        }
        LOGGER.info(builder.toString());
    }
    
    private static String getStackMsg(Object msg) {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        if (traces.length < 1) {
            return "";
        }
        boolean flag = false;
        boolean flag2 = false;
        for (StackTraceElement ste : traces) {
            if (flag) {
                if (!flag2) {
                    flag2 = true;
                } else {
                    return String.format("%s\r\n%s", ste.toString(), msg);
                }
            }
            if (THIS_NAME.equalsIgnoreCase(ste.getClassName())) {
                flag = true;
            }
        }
        return "";
    }
    
    public static void debug(Object s) {
        LOGGER.debug(getStackMsg(s));
    }
    
    public static void debug(Object s, Throwable t) {
        LOGGER.debug(getStackMsg(s), t);
    }
    
    public static void debug(Object s, Object... params) {
        LOGGER.debug(getStackMsg(s), params);
    }
    
    public static void info(Object s) {
        LOGGER.info(getStackMsg(s));
    }
    
    public static void info(Object s, Throwable t) {
        LOGGER.info(getStackMsg(s), t);
    }
    
    public static void info(Object s, Object... params) {
        LOGGER.info(getStackMsg(s), params);
    }
    
    public static void warn(Object s) {
        LOGGER.warn(getStackMsg(s));
    }
    
    public static void warn(Object s, Throwable t) {
        LOGGER.warn(getStackMsg(s), t);
    }
    
    public static void warn(Object s, Object... params) {
        LOGGER.warn(getStackMsg(s), params);
    }
    
    public static void error(Object s) {
        LOGGER.error(getStackMsg(s));
    }
    
    public static void error(Object s, Throwable t) {
        LOGGER.error(getStackMsg(s), t);
    }
    
    public static void error(Object s, Object... params) {
        LOGGER.error(getStackMsg(s), params);
    }
}
