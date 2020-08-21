package com.dianping.cat.alarm.spi.sender;

import com.dianping.cat.Cat;
import com.dianping.cat.alarm.sender.entity.Sender;
import com.dianping.cat.alarm.spi.AlertChannel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.codehaus.plexus.util.StringUtils;
import org.unidal.helper.Files;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DingTalkSender extends AbstractSender {

    private static final Gson g = new Gson();

    public static final String ID = AlertChannel.DINGDING.getName();

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public boolean send(SendMessageEntity message) {
        Sender sender = querySender();
//        boolean batchSend = sender.getBatchSend();
        boolean result = false;

        List<String> dingTalkUris = message.getReceivers();

        for (String dingTalkUri : dingTalkUris) {
            boolean success = sendDingTalk(message, dingTalkUri, sender);
            result = result || success;
        }

        return result;
    }

    /**
     * 钉钉机器人发送
     * @param message
     * @param dingTalkUri  可能包含备注
     * @param sender
     * @return
     */
    private boolean sendDingTalk(SendMessageEntity message, String dingTalkUri, Sender sender) {
        String paramsStr = null;
        String domain = message.getGroup();
        String title = message.getTitle().replaceAll(",", " ");
        String content = message.getContent();
        String time = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        String webHookURL = sender.getUrl();

//        if (dingTalkUri.contains("=")) {
//            dingTalkUri = dingTalkUri.split("=")[1];
//        }

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("msgtype", "text");
        content = "CAT报警：" + " - " + title + " \n " + content;
        try {
            String[] paramArr = {"content="+ URLEncoder.encode(content, "utf-8"),
                    "uri="+URLEncoder.encode(dingTalkUri, "utf-8"), "time="+URLEncoder.encode(time, "utf-8")};
            paramsStr = StringUtils.join(paramArr, "&");
        } catch (Exception e) {
            Cat.logError(e);
        }
        return httpJsonPostSend(sender.getSuccessCode(), webHookURL, paramsStr);
    }


    private boolean httpJsonPostSend(String successCode, String urlStr, String paramsStr) {
        URL url = null;
        InputStream in = null;
        OutputStreamWriter writer = null;
        URLConnection conn = null;
        boolean sendSuccess = false;
        try {
            url = new URL(urlStr);
            conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(3000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(paramsStr);
            writer.flush();

            in = conn.getInputStream();
            StringBuilder sb = new StringBuilder();

            sb.append(Files.forIO().readFrom(in, "utf-8")).append("");
            if (sb.toString().contains(successCode)) {
                sendSuccess = true;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            m_logger.error("Dingding send error: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
            if (!sendSuccess) {
                Cat.logError(urlStr + "---" + paramsStr, new RuntimeException("发送钉钉告警失败！"));
            }
        }
    }


    private static void close(HttpURLConnection closeable) {
        if (closeable != null) {
            try {
                closeable.disconnect();
            } catch (Exception e) {

            }
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {

            }
        }
    }

    private static String readBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int readCount = 0;
        while ((readCount = inputStream.read(buffer)) > -1) {
            out.write(buffer, 0, readCount);
        }
        return out.toString("UTF-8");
    }

    private static class DingTalkResponseError extends Exception {

        public DingTalkResponseError(String errMsg) {
            super(errMsg);
        }

    }

}