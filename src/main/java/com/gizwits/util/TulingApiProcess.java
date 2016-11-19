package com.gizwits.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by feel on 16/3/27.
 */
public class TulingApiProcess {


    public static String getTulingResult(String content, String apiUrl) {

        /**
         * 此处为图灵api接口，参数key需要自己去注册申请，先以11111111代替
         */

        String param = "";
        try {
            param = apiUrl + URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } //将参数转为url编码

        /**
         * 发送httpget请求
         */
        HttpGet request = new HttpGet(param);
        String result = "";
        try {
            HttpResponse response = HttpClients.createDefault().execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /** 请求失败处理 */
        if (null == result) {
            return "对不起，你说的话真是太高深了……";
        }

        try {
            JSONObject json = new JSONObject(result);
            //以code=100000为例，参考图灵机器人api文档
            if (100000 == json.getInt("code")) {
                result = json.getString("text");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }
}
