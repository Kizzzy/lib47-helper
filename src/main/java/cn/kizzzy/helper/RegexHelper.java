package cn.kizzzy.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    private static final String REGEX_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    private static final String REGEX_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    private static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    private static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";
    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    private static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
    private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    private static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    public static boolean checkValid(String regex, String s) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 是否是邮件地址
     */
    public static boolean isEmail(String s) {
        return checkValid(REGEX_EMAIL, s);
    }

    /**
     * 是否是手机号码
     */
    public static boolean isPhoneNumber(String s) {
        return checkValid(REGEX_PHONE, s);
    }

    /**
     * 是否是中文
     */
    public static boolean isChinese(String s) {
        return checkValid(REGEX_CHINESE, s);
    }

    /**
     * 是否是用户名
     */
    public static boolean isUsername(String s) {
        return checkValid(REGEX_USERNAME, s);
    }

    /**
     * 是否是密码
     */
    public static boolean isPassword(String s) {
        return checkValid(REGEX_PASSWORD, s);
    }

    public static boolean isMobile(String s) {
        return checkValid(REGEX_MOBILE, s);
    }

    public static boolean isIdCard(String s) {
        return checkValid(REGEX_ID_CARD, s);
    }

    public static boolean isUrl(String s) {
        return checkValid(REGEX_URL, s);
    }

    public static boolean isIpAddr(String s) {
        return checkValid(REGEX_IP_ADDR, s);
    }
}
