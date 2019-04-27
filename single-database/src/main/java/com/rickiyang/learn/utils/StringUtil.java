package com.rickiyang.learn.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class StringUtil {
    private static final char UNDERLINE = '_';

    private static final String PHONE_REGEX = "^(1[3-9])\\d{9}$";

    /**
     * 姓名校验 （只允许空字符串，或最长50个字符长度字符串一个汉字算一个字符，如非空则只允许包含 空格、中文、字母、下划线、数字、名字分隔符·）
     */
    private static final String NICKNAME = "^[\\u4e00-\\u9fa5\\ \\w·]{0,50}$";

    /**
     * 电话密文正则
     */
    private static final String ENCRYPT = "^[A-Za-z\\d+/]{10,98}([A-Za-z\\d+/][A-Za-z\\d+/=]|==)$";

    /**
     * 纯数字正则
     */
    private static final String NUMERIC = "^\\d+$";

    /**
     * 电话明文正则
     */
    private static final String REGEX_PHONENUMBER = "(^|(.*(\\D)+))([1](([3][0-9])|([4][5,7,9])|([5][4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8})($|((\\D)+.*))";

    public static String trim(String source, char element) {
        boolean beginIndexFlag;
        boolean endIndexFlag;
        do {
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element)
                    : source.length();
            source = source.substring(beginIndex, endIndex);
            beginIndexFlag = (source.indexOf(element) == 0);
            endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
        } while (beginIndexFlag || endIndexFlag);
        return source;
    }

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 检查是否是合法的手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        return phone != null && phone.length() > 0 && Pattern.matches(PHONE_REGEX, phone);
    }

    /**
     * 根据邮箱截取邮箱前缀
     *
     * @param email
     * @return 邮箱前缀
     */
    public static String getEmailPrefix(String email) {
        if (StringUtils.isNotEmpty(email)) {
            String[] temp = email.split("@");
            return temp[0];
        }
        return email;
    }

    /**
     * 判断字符串是否合法电话密文
     *
     * @param phoneEncrypt 电话密文字符串
     * @return
     */
    public static boolean isPhoneEncrypt(String phoneEncrypt) {
        // 如字符串非空则判断是否纯数字以及是否标准BASE64格式， 但由于不是所有的BASE64都以=结尾， 所以并未直接判断=结尾
        return null == phoneEncrypt || phoneEncrypt.isEmpty()
                || (phoneEncrypt.matches(ENCRYPT) && !phoneEncrypt.matches(NUMERIC));
    }

    /**
     * 判断字符串是否合法姓名（只允许空字符串，或最长50个字符长度字符串一个汉字算一个字符，如非空则只允许包含 空格、中文、数字、数字、姓名分隔符·）
     *
     * @param nickname 姓名字符串
     * @return
     */
    public static boolean isNickname(String nickname) {
        // 只允许空字符串，或最长50个字符长度字符串一个汉字算一个字符，如非空则只允许包含 空格、中文、字母、下划线、数字、名字分隔符·
        return null == nickname || nickname.isEmpty() || nickname.matches(NICKNAME);
    }

    /**
     * 判断是否包含手机号
     *
     * @param phoneNumber 手机号
     * @return 是否手机号
     */
    public static boolean hasPhoneNumber(String phoneNumber) {
        return Pattern.matches(REGEX_PHONENUMBER, phoneNumber);
    }

    /**
     * 替换手机号为指定字符串
     *
     * @param phoneNumber 手机号
     * @param replaceStr  替换字符
     * @return 替换后的串
     */
    public static String replacePhoneNumber(String phoneNumber, String replaceStr) {
        return StringUtils.replaceAll(phoneNumber, REGEX_PHONENUMBER, "$1" + replaceStr + "$14");
    }

}
