package cn.kizzzy.helper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AesHelper extends HexHelper {
    public static final int AES_128 = 128;
    public static final int AES_192 = 192;
    public static final int AES_256 = 256;
    
    /**
     * 1.构造密钥生成器, 指定为AES算法,不区分大小写
     * 2.初始化密钥生成器, 根据传入的字节数组, 生成一个128位的随机源
     * 3.产生原始对称密钥
     * 4.获得原始对称密钥的字节数组
     * 5.根据字节数组生成AES密钥
     * 6.根据指定算法AES自成密码器
     * 7.初始化密码器, 第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作, 第二个参数为使用的KEY
     * 8.将加密并编码后的内容解码成字节数组
     */
    private static byte[] aes_execute(byte[] content, byte[] key, int mode, int bit) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key);
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(bit, secureRandom);
            SecretKey secretKey = keygen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, secretKeySpec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            LogHelper.error(null, e);
        }
        return null;
    }
    
    /**
     * AES加密
     */
    private static byte[] aes_encode(byte[] content, byte[] key, int bit) {
        return aes_execute(content, key, Cipher.ENCRYPT_MODE, bit);
    }
    
    /**
     * AES解密
     */
    private static byte[] aes_decode(byte[] content, byte[] key, int bit) {
        return aes_execute(content, key, Cipher.DECRYPT_MODE, bit);
    }
    
    /**
     * AES加密
     */
    private static String aes_encode(String content, String key, int bit) {
        byte[] var0 = content.getBytes(StandardCharsets.UTF_8);
        byte[] var1 = key.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(aes_encode(var0, var1, bit));
    }
    
    /**
     * AES解密
     */
    private static String aes_decode(String content, String key, int bit) {
        byte[] var0 = Base64.getDecoder().decode(content);
        byte[] var1 = key.getBytes(StandardCharsets.UTF_8);
        return new String(aes_decode(var0, var1, bit), StandardCharsets.UTF_8);
    }
    
    
    public static byte[] encode(byte[] content, byte[] key, int bit) {
        switch (bit) {
            case AES_128:
            case AES_192:
            case AES_256:
                return aes_encode(content, key, bit);
            default:
                throw new RuntimeException(bit + " bit is not support, ");
        }
    }
    
    public static byte[] decode(byte[] content, byte[] key, int bit) {
        switch (bit) {
            case AES_128:
            case AES_192:
            case AES_256:
                return aes_decode(content, key, bit);
            default:
                throw new RuntimeException(bit + " bit is not support, ");
        }
    }
    
    public static String encode(String content, String key, int bit) {
        switch (bit) {
            case AES_128:
            case AES_192:
            case AES_256:
                return aes_encode(content, key, bit);
            default:
                throw new RuntimeException(bit + " bit is not support, ");
        }
    }
    
    public static String decode(String content, String key, int bit) {
        switch (bit) {
            case AES_128:
            case AES_192:
            case AES_256:
                return aes_decode(content, key, bit);
            default:
                throw new RuntimeException(bit + " bit is not support, ");
        }
    }
    
    /**
     * AES加密
     */
    public static String aes128_encode(String content, String key) {
        return aes_encode(content, key, 128);
    }
    
    /**
     * AES解密
     */
    public static String aes128_decode(String content, String key) {
        return aes_decode(content, key, 128);
    }
    
    /**
     * AES加密
     */
    public static String aes192_encode(String content, String key) {
        return aes_encode(content, key, 192);
    }
    
    /**
     * AES解密
     */
    public static String aes192_decode(String content, String key) {
        return aes_decode(content, key, 192);
    }
    
    /**
     * AES加密
     */
    public static String aes256_encode(String content, String key) {
        return aes_encode(content, key, 256);
    }
    
    /**
     * AES解密
     */
    public static String aes256_decode(String content, String key) {
        return aes_decode(content, key, 256);
    }
}
