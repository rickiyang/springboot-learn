package com.rickiyang.learn.utils.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Base64 util
 **/
public class Base64 {
    static Logger logger = LoggerFactory.getLogger(Base64.class);

    /**
     * base64 编码
     * @param data
     * @return
     */
    public static String encode(String data){
        try {
            return encode(data.getBytes("utf-8"));
        }catch (Exception e){
            logger.error("error ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * base64 编码
     * @param data
     * @return
     */
    public static String encode(byte[] data){
        try{
            BASE64Encoder base64en = new BASE64Encoder();
            String output = base64en.encode(data);
            return output;
        }catch (Exception e){
            logger.error("error ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * base64 编码
     * @param data
     * @return
     */
    public static String encodeBuffer(byte[] data){
        try{
            BASE64Encoder base64en = new BASE64Encoder();
            String output = base64en.encodeBuffer(data);
            return output;
        }catch (Exception e){
            logger.error("error ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * base64 解码
     * @param data
     * @return
     */
    public static byte[] decodeBuffer(String data){
        try{
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] valueByte = decoder.decodeBuffer(data);
            return valueByte;
        }catch (Exception e){
            logger.error("error ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * base64 解码
     * @param data
     * @return
     */
    public static byte[] decode(byte[] data){
        return decodeBuffer(new String(data));
    }
}
