package cn.kizzzy.helper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MacHelper extends HexHelper {
    
    /**
     * MAC 加密算法
     */
    private static String mac_encode(byte[] data, byte[] key, String algorithm) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(keySpec);
            byte[] bytes = mac.doFinal(data);
            return new String(bytes2Hex(bytes));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * HmacMD5 加密
     */
    public static String hmac_md5(String data, String key) {
        return mac_encode(data.getBytes(), key.getBytes(), "HmacMD5");
    }
    
    /**
     * HmacSHA1 加密
     */
    public static String hmac_sha1(String data, String key) {
        return mac_encode(data.getBytes(), key.getBytes(), "HmacSHA1");
    }
    
    /**
     * HmacSHA224 加密
     */
    public static String hmac_sha224(String data, String key) {
        return mac_encode(data.getBytes(), key.getBytes(), "HmacSHA224");
    }
    
    /**
     * HmacSHA256 加密
     */
    public static String hmac_sha256(String data, String key) {
        return mac_encode(data.getBytes(), key.getBytes(), "HmacSHA256");
    }
    
    /**
     * HmacSHA384 加密
     */
    public static String hmac_sha384(String data, String key) {
        return mac_encode(data.getBytes(), key.getBytes(), "HmacSHA384");
    }
    
    /**
     * HmacSHA512 加密
     */
    public static String hmac_sha512(String data, String key) {
        return mac_encode(data.getBytes(), key.getBytes(), "HmacSHA512");
    }
}
