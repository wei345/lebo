package com.lebo.redis;

/**
 * @author: Wei Liu
 * Date: 13-12-13
 * Time: PM2:52
 */
public class RedisKeys {

    public static String getLeboSessionKey(String userId, String sessionId){
        return new StringBuilder("lebo.session:")
                .append(userId)
                .append(".")
                .append(sessionId)
                .toString();
    }

    public static String getAlipayNotifyIdKey(String notifyId){
        return "alipay.notify:" + notifyId;
    }
}
