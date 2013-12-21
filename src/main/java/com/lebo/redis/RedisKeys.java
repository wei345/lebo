package com.lebo.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-12-13
 * Time: PM2:52
 */
public class RedisKeys {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getLeboSessionKey(String userId, String sessionId) {
        return new StringBuilder("lebo.session:")
                .append(userId)
                .append(".")
                .append(sessionId)
                .toString();
    }

    public static String getAlipayNotifyIdKey(String notifyId) {
        return "alipay.notify:" + notifyId;
    }

    public static String getActiveUserKey() {
        return "active.user:" + sdf.format(new Date());
    }
}
