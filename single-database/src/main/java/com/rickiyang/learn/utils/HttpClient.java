package com.rickiyang.learn.utils;

import okhttp3.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static final int DEFAULT_TIME_OUT = 5;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String CHARSET_NAME = "UTF-8";

    private OkHttpClient okHttpClient;

    public HttpClient(){
        okHttpClient = new OkHttpClient.Builder().connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS).build();
    }

    public HttpClient(int time_out) {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(time_out, TimeUnit.SECONDS).build();
    }

    public HttpClient(int time_out, TimeUnit timeUnit) {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(time_out, timeUnit).build();
    }

    public String get(String url) throws Exception {
        Request.Builder builder = new Request.Builder().url(url);

        Response response = okHttpClient.newCall(builder.build()).execute();
        if(!response.isSuccessful()){
            throw new IOException("Unexpected code : " + response);
        }
        String result = response.body().string();
        return result;
    }

    public String post(String url, String json) throws Exception {
       return post(url, json, null);
    }


    public String get(String url, Map<String, String> headers) throws Exception {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null && headers.size() != 0 ) {
            for (String s : headers.keySet()) {
                if(headers.get(s) != null) {
                    builder.addHeader(s, headers.get(s));
                }
            }
        }
        Response response = okHttpClient.newCall(builder.build()).execute();
        String result = response.body().string();
        return  result;
    }


    public String post(String url, String json, Map<String, String> headers) throws Exception {
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder().url(url).post(body);
        if (headers != null && headers.size() != 0 ) {
            for (String s : headers.keySet()) {
                if (headers.get(s) != null) {
                    builder.addHeader(s, headers.get(s));
                }
            }
        }
        Response response = okHttpClient.newCall(builder.build()).execute();
        String result = response.body().string();
        return  result;
    }

    public String post(String url, Map<String, String> params) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry< String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        String result = response.body().string();
        return result;

    }

    public String get(String url,Map<String, String> headers, Map<String, String> params) throws IOException {
        if (params != null && params.size() != 0 ) {
            url = attachHttpGetParams(url, params);
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null && headers.size() != 0 ) {
            for (String s : headers.keySet()) {
                builder.addHeader(s, headers.get(s));
            }
        }

        Response response = okHttpClient.newCall(builder.build()).execute();
        if(!response.isSuccessful()){
            throw new IOException("Unexpected code : " + response);
        }
        String result = response.body().string();
        return result;

    }


    /**
     * 这里使用了HttpClinet的API。只是为了方便
     * @param params
     * @return
     */
    private static String formatParams(List<BasicNameValuePair> params){
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }
    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     * @param url
     * @param params
     * @return
     */
    private static String attachHttpGetParams(String url, Map<String, String> params){

        List<BasicNameValuePair> paramsList = new ArrayList<>();
        for (String s : params.keySet()) {
            paramsList.add(new BasicNameValuePair(s, params.get(s)));
        }

        return url + "?" + formatParams(paramsList);
    }



}


