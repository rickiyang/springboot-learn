package com.rickiyang.learn.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * about this class
 *
 **/
public class AESCBC {

    private static final String FULL_MODE = "AES/CBC/PKCS5Padding";

    /**
     * 加密明文
     *
     * @param plain 明文
     * @param hexKey 秘钥
     * @param hexIv 向量
     */
    public static String encrypt(String plain, String hexKey, String hexIv) {
        return encrypt(plain, hexKey, "UTF-8", hexIv);
    }

    /**
     * 加密明文
     *
     * @param plain 明文
     * @param hexKey 密钥
     * @param charset 字符集
     * @param hexIv 向量
     */
    public static String encrypt(String plain, String hexKey, String charset, String hexIv) {
        byte[] keys = Base64.decodeBuffer(hexKey);
        byte[] hexIvs = Base64.decodeBuffer(hexIv);
        return encrypt(plain, keys, charset, hexIvs);
    }

    /**
     * 加密明文
     *
     * @param plain 明文
     * @param key 密钥
     * @param charset 字符集
     * @param hexIv 向量
     */
    public static String encrypt(String plain, byte[] key, String charset, byte[] hexIv) {
        try {
            Cipher aes = Cipher.getInstance(FULL_MODE);
            SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
            // 向量
            IvParameterSpec iv = new IvParameterSpec(hexIv);
            aes.init(Cipher.ENCRYPT_MODE, aesKey, iv);
            byte[] secret = aes.doFinal(plain.getBytes(charset));
            return Base64.encode(secret);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     * @param secret 密文
     * @param hexKey 秘钥
     * @param hexIv 向量
     */
    public static String decrypt(String secret, String hexKey, String hexIv) {
        byte[] keys = Base64.decodeBuffer(hexKey);
        byte[] hexIvs = Base64.decodeBuffer(hexIv);
        return decrypt(secret, keys, "utf-8", hexIvs);
    }

    /**
     * 解密
     * @param secret 密文
     * @param key 密钥
     * @param charset 字符集
     * @param hexIv 向量
     * @return
     * @throws Exception
     */
    public static String decrypt(String secret, byte[] key, String charset, byte[] hexIv) {
        try {
            Cipher aes = Cipher.getInstance(FULL_MODE);
            SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
            IvParameterSpec iv = new IvParameterSpec(hexIv);
            aes.init(Cipher.DECRYPT_MODE, aesKey, iv);
            byte[] plain = aes.doFinal(Base64.decodeBuffer(secret));
            return new String(plain, charset);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }catch (InvalidAlgorithmParameterException e){
            throw new RuntimeException(e);
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }catch (InvalidKeyException e){
            throw new RuntimeException(e);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

}
