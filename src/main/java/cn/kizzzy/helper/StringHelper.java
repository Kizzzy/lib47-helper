package cn.kizzzy.helper;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper extends HexHelper {
    
    public static boolean isLetter(char c) {
        return c / 0x80 == 0;
    }
    
    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.length() == 0 || s.trim().length() == 0)
            return true;
        return false;
    }
    
    public static boolean isNotNullAndEmpty(String s) {
        return !isNullOrEmpty(s);
    }
    
    public static String chars2String(char[] chars, char end) {
        StringBuilder builder = new StringBuilder();
        for (char ch : chars) {
            if (ch == end) {
                break;
            }
            builder.append(ch);
        }
        return builder.toString();
    }
    
    public static int lengthOfByte(String s) {
        if (s == null) {
            return 0;
        }
        char[] chars = s.toCharArray();
        int len = 0;
        for (char temp : chars) {
            len++;
            if (!isLetter(temp)) {
                len++;
            }
        }
        return len;
    }
    
    public static double lengthOfChar(String s) {
        double len = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < s.length(); i++) {
            String temp = s.substring(i, i + 1);
            if (temp.matches(chinese)) {
                len += 1;
            } else {
                len += 0.5;
            }
        }
        return len;
    }
    
    public static String firstUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public static String format(String pattern, Object... args) {
        return MessageFormat.format(pattern, args);
    }
    
    public static boolean split(String str, Consumer<String[]> callback) {
        return split(str, "\\|", ",", callback);
    }
    
    /**
     * @param str      origin string
     * @param var0     first seperator
     * @param var1     second seperator
     * @param callback consumer
     * @return
     */
    public static boolean split(String str, String var0, String var1, Consumer<String[]> callback) {
        try {
            if (isNotNullAndEmpty(str)) {
                String[] var2 = str.split(var0);
                for (String var3 : var2) {
                    callback.accept(var3.split(var1));
                }
            }
        } catch (Exception e) {
            LogHelper.error(null, e);
            return false;
        }
        return true;
    }
    
    public static Map<String, String> parseCookie(String cookie) {
        Map<String, String> kvs = new HashMap<>();
        
        if (StringHelper.isNotNullAndEmpty(cookie)) {
            String[] lines = cookie.split("\n");
            for (String line : lines) {
                Pattern pattern = Pattern.compile("([^:]*):\\s?(.*)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    kvs.put(key, value);
                }
            }
        }
        
        return kvs;
    }
}
