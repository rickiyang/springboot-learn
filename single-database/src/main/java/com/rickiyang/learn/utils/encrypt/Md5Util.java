package com.rickiyang.learn.utils.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 提供对字符串的md5-->string
 */
public class Md5Util {

    static Logger logger = LoggerFactory.getLogger(Md5Util.class);


    /**
     * 利用MD5进行加密
     *
     * @param data 待加密的字符串
     * @return 加密后的字符串, null if exception occur
     */
    public static String encodeAndBase64(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            String output = base64en.encode(md5.digest(data.getBytes("utf-8")));
            return output;
        } catch (Exception e) {
            logger.error("fail to encodeAndMd5 in md5:", e);
        }
        return null;
    }

    public static byte[] digest(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(data.getBytes("utf-8"));
        } catch (Exception e) {
            logger.error("fail to encodeAndMd5 in md5:", e);
        }
        return null;
    }

    public static String getContentMd5(byte[] src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(src);
            byte[] args = md5.digest();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < args.length; ++i) {
                sb.append(Integer.toHexString(255 & args[i] | -256).substring(6));
            }
            String output = sb.toString();
            return output;
        } catch (NoSuchAlgorithmException e) {
            logger.error("error with getContentMd5, ", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean checkMd5(String md5, byte[] src) {
        String srcMd5 = getContentMd5(src);
        return md5.equalsIgnoreCase(srcMd5);
    }

}
