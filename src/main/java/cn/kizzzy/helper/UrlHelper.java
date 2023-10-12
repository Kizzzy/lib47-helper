package cn.kizzzy.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlHelper extends HexHelper {
    
    private static final Pattern PATTERN_HEADERS
        = Pattern.compile("^(?<key>[^\\s]+):\\s?(?<val>[^\\s]+)$");
    
    /**
     * Url编码
     */
    public static String url_encode(String str) throws UnsupportedEncodingException {
        return url_encode(str, "UTF-8");
    }
    
    /**
     * Url解码
     */
    public static String url_encode(String str, String enc) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, enc);
    }
    
    /**
     * Url解码
     */
    public static String url_decode(String str) throws UnsupportedEncodingException {
        return url_decode(str, "UTF-8");
    }
    
    /**
     * Url编码
     */
    public static String url_decode(String str, String enc) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, enc);
    }
    
    /**
     * 解析请求
     */
    public static Map<String, String> parseHeader(String str) {
        Map<String, String> map = new HashMap<>();
        if (StringHelper.isNotNullAndEmpty(str)) {
            String[] var0 = str.split("\n");
            for (String var1 : var0) {
                Matcher matcher = PATTERN_HEADERS.matcher(var1);
                if (matcher.find()) {
                    String key = matcher.group("key");
                    String val = matcher.group("val");
                    if (StringHelper.isNotNullAndEmpty(key) && StringHelper.isNotNullAndEmpty(val)) {
                        map.put(key, val);
                    }
                }
            }
        }
        return map;
    }
}
