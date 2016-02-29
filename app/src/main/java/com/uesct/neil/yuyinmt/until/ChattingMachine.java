package com.uesct.neil.yuyinmt.until;

import android.content.Context;

import com.google.gson.Gson;
import com.uesct.neil.yuyinmt.bean.ChattingMsgItem;
import com.uesct.neil.yuyinmt.bean.Result;
import com.uesct.neil.yuyinmt.exception.CommonException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;


/**
 * Created by Neil on 2016/2/26.
 */
public class ChattingMachine {
    private static String API_KEY = "6b2c417e933171f306cd4e6c1801c00a";
    private static String URL = "http://www.tuling123.com/openapi/api";

    public ChattingMachine(Context context) {
    }

    public ChattingMsgItem sendMessage(String content) throws CommonException {
        ChattingMsgItem message = new ChattingMsgItem("", ChattingMsgItem.ChattingMsgType.MT);
        String url = setParams(content);
        String res = doGet(url);
        Gson gson = new Gson();
        Result result = gson.fromJson(res, Result.class);

        if (result.getCode() > 400000 || result.getText() == null
                || result.getText().trim().equals("")) {
            message.setContent("该功能等待开发...");
        } else {
            message.setContent(result.getText());
        }
        message.setDate(new Date());

        return message;
    }

    /**
     * 拼接Url
     *
     * @param msg
     * @return
     */
    private static String setParams(String msg) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL + "?key=" + API_KEY + "&info=" + msg;
    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     */
    private static String doGet(String urlStr) throws CommonException {
        java.net.URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new CommonException("服务器连接错误！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("服务器连接错误！");
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }

    }


}
