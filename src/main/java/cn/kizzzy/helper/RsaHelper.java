package cn.kizzzy.helper;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RsaHelper {
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String RSA_ALGORITHM = "RSA";
    
    public static Map<String, String> createKeys(int keySize) {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }
        
        // 初始化KeyPairGenerator对象,密钥长度
        generator.initialize(keySize); // keySize 可以为1024
        // 生成密匙对
        KeyPair keyPair = generator.generateKeyPair();
        
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = new String(Base64.getEncoder().encode(publicKey.getEncoded()), CHARSET);
        
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = new String(Base64.getEncoder().encode(privateKey.getEncoded()), CHARSET);
        
        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        
        return keyPairMap;
    }
    
    /**
     * 得到公钥
     * 通过X509编码的Key指令获得公钥对象
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.getBytes(CHARSET)));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }
    
    /**
     * 得到私钥
     * 通过PKCS#8编码的Key指令获得私钥对象
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes(CHARSET)));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }
    
    /**
     * 公钥加密
     */
    public static String publicEncrypt(String string, RSAPublicKey publicKey) {
        byte[] input = string.getBytes(CHARSET);
        byte[] data = rsaExecute(input, publicKey, Cipher.ENCRYPT_MODE);
        return new String(Base64.getEncoder().encode(data), CHARSET);
    }
    
    /**
     * 私钥加密
     */
    public static String privateEncrypt(String string, RSAPrivateKey privateKey) {
        byte[] input = string.getBytes(CHARSET);
        byte[] data = rsaExecute(input, privateKey, Cipher.ENCRYPT_MODE);
        return new String(Base64.getEncoder().encode(data), CHARSET);
    }
    
    /**
     * 公钥解密
     */
    
    public static String publicDecrypt(String string, RSAPublicKey publicKey) {
        byte[] input = Base64.getDecoder().decode(string.getBytes(CHARSET));
        byte[] data = rsaExecute(input, publicKey, Cipher.DECRYPT_MODE);
        return new String(data, CHARSET);
    }
    
    /**
     * 私钥解密
     */
    public static String privateDecrypt(String string, RSAPrivateKey privateKey) {
        byte[] input = Base64.getDecoder().decode(string.getBytes(CHARSET));
        byte[] data = rsaExecute(input, privateKey, Cipher.DECRYPT_MODE);
        return new String(data, CHARSET);
    }
    
    /**
     * 加/解密逻辑
     */
    private static byte[] rsaExecute(byte[] input, Key key, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(mode, key);
            
            int keySize = ((RSAKey) key).getModulus().bitLength();
            int maxBlock = keySize / 8 - (mode == Cipher.DECRYPT_MODE ? 0 : 11);
            
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int offSet = 0;
                byte[] buff;
                int i = 0;
                while (input.length > offSet) {
                    if (input.length - offSet > maxBlock) {
                        buff = cipher.doFinal(input, offSet, maxBlock);
                    } else {
                        buff = cipher.doFinal(input, offSet, input.length - offSet);
                    }
                    out.write(buff, 0, buff.length);
                    offSet = (++i) * maxBlock;
                }
                return out.toByteArray();
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }
}
