package com.rickiyang.learn.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *cookie工具类
 */
public class CookieUtil {

    private static final String SSO = "SSO";

    /**
     * 设置COOKIE
     *
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain, int maxAge) {
        // 添加cookie
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取cookie
     *
     * @param request
     * @param name
     * @return: void
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        // 获取cookies
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 获得CookieDomain
     *
     * @param domain
     */
    public static String getCookieDomain(String domain) {
        if (null == domain || "".equals(domain)) {
            domain = "baidu.com";
        }
        return domain;
    }

    /**
     * 获得CookieDomain
     *
     * @param request
     */
    public static String getToken(HttpServletRequest request) {
        Cookie cookie = getCookie(request, SSO);
        return cookie.getValue();
    }
}
