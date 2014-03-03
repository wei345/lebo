package com.lebo.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-12-13
 * Time: PM2:52
 */
public class RedisKeys {

    //Spring Expression Language (SpEL)
    //http://docs.spring.io/spring/docs/3.0.x/reference/expressions.html
    public static final String ROBOT_GROUP_SPEL = "'robot.groups'";
    public static final String ROBOT_SAYING_TAG_SPEL = "'robot.saying.tags'";
    public static final String USER_BANNED_SPEL = "'user.' + #id + '.banned'";
    public static final String GOODS_ID_SPEL = "'goods.' + #name + '.id'";

    public static final String CACHE_NAME_DEFAULT = "cache";

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
