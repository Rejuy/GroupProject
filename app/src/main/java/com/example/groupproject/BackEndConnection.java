package com.example.groupproject;


import java.util.HashMap;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
//import com.google.common.collect.Maps;

/**
 * @author smile
 * @date 2020/9/30
 * @info 调用接口测试类
 */
public class BackEndConnection {

    public static String test(String url , Object obj) {
//        //get请求
//        HttpRequest getRequest = HttpRequest.get("http://...");
//        //设置请求类型
//        getRequest.contentType(ContentType.JSON.getValue());
//        //设置请求头 k=v
//        getRequest.header("","");
//        //设置请求体 str
//        getRequest.body("");
//        //发送请求并获得响应
//        HttpResponse getResponse = getRequest.execute();
//        //响应体
//        String getBody = getResponse.body();


        //post请求
        HttpRequest postRequest = HttpRequest.post(url);
        //设置请求类型
        postRequest.contentType(ContentType.JSON.getValue());
        //设置请求头 k=v
        postRequest.header("");
        //设置请求体 str
        String jsonStr = JSONUtil.toJsonStr(obj);
        postRequest.body(jsonStr);
        //发送请求并获得响应
        HttpResponse postResponse = postRequest.execute();
        //响应体
        String postBody = postResponse.body();
        return postBody;
    }
}



