package cn.kizzzy.helper;

import java.security.MessageDigest;

public class MdHelper extends HexHelper {
    
    /**
     * 加密算法
     */
    private static String encode(String algorithm, String charsetName, String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes(charsetName));
            byte[] hash = messageDigest.digest();
            return new String(bytes2Hex(hash));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 加密算法
     */
    private static String encode(String algorithm, String value) {
        return encode(algorithm, "UTF-8", value);
    }
    
    /**
     * MD5加密(返回32位小写)
     */
    public static String md5_encode(String str) {
        return encode("md5", str);
    }
    
    /**
     * SHA1加密
     */
    public static String sha1_encode(String str) {
        return encode("SHA1", str);
    }
    
    /**
     * SHA-224加密
     */
    public static String sha224_encode(String str) {
        return encode("SHA-224", str);
    }
    
    /**
     * SHA-256加密
     */
    public static String sha256_encode(String str) {
        return encode("SHA-256", str);
    }
    
    /**
     * SHA-384加密
     */
    public static String sha384_encode(String str) {
        return encode("SHA-384", str);
    }
    
    /**
     * SHA-512加密
     */
    public static String sha512_encode(String str) {
        return encode("SHA-512", str);
    }
}
