package com.rickiyang.learn.utils.encrypt;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加解密工具类
 *
 **/
public class AES {

    static String ALGO = "AES";

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key 加密密码
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGO);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, seckey);// 初始化
            byte[] result = cipher.doFinal(data);
            return result; // 加密
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key 解密密钥
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGO);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, seckey);// 初始化
            byte[] result = cipher.doFinal(data);
            return result; // 加密
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    /**
     * 加密并用Base64编码
     * @param data 待加密处理字符串
     * @param key 加密密钥
     * @return
     */
    public static String encryptToBase64(String data, String key) {
        try {
            byte[] valueByte = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
            BASE64Encoder base64en = new BASE64Encoder();
            return new String(base64en.encode(valueByte));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    /**
     * 先进行Base64解码，再进行AES解密
     *
     * @param data 待解密字符
     * @param key 解密密钥
     * @return
     */
    public static String decryptFromBase64(String data, String key) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] originalData = decoder.decodeBuffer(data);
            byte[] valueByte = decrypt(originalData, key.getBytes("UTF-8"));
            return new String(valueByte, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    /**
     * 生成随机密钥
     *
     * @return
     */

    public static byte[] genarateRandomKey() {
        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance(ALGO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(" genarateRandomKey fail!", e);
        }
        SecureRandom random = new SecureRandom();
        keygen.init(random);
        Key key = keygen.generateKey();
        return key.getEncoded();
    }

    /**
     * 生成随机密钥并用BASE64编码
     *
     * @return
     */
    public static String genarateRandomKeyWithBase64() {
        BASE64Encoder encoder = new BASE64Encoder();
        return new String(encoder.encode(genarateRandomKey()));
    }


}
