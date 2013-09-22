package com.lebo.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 记录处理每个请求总共花费的时间和在每个方法里花费的时间。
 * 时间格式：最近花费时间／方法开始至结束花费时间／请求总花费时间
 *
 * @author liuwei
 */
@SuppressWarnings("unused")
@Component
@Aspect
public class RequestTimeLogger {
    private static Logger logger = LoggerFactory.getLogger(RequestTimeLogger.class);
    private static String REQUEST_ID = "requestId";
    private static String START = "start";
    private static String LAST = "last";
    private static String NOW = "now";
    private static String REQUEST_URI = "requestUri";
    private static Random r = new Random();

    private static ThreadLocal<Map<String, Object>> contextTL = new ThreadLocal<Map<String, Object>>() {
        @Override
        public Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static void begin(HttpServletRequest request) {
        // 构造请求的唯一标识，用于在日志中追踪请求处理过程
        String requestId = request.getSession().getId() + "_" + new Date().getTime() + "_" + r.nextInt(100000000);
        contextTL.get().put(REQUEST_ID, requestId);

        contextTL.get().put(REQUEST_URI, request.getRequestURI());

        long time = System.currentTimeMillis();
        contextTL.get().put(START, time);
        contextTL.get().put(LAST, time);

        contextTL.get().put(NOW, time);
        log("begin", null);
    }

    public static void end() {
        // 计算花费时间时，使用一致的“现在时间”
        long time = System.currentTimeMillis();
        contextTL.get().put(NOW, time);

        // 方法中花费的时间
        long methodBeginTime = (Long) contextTL.get().get(START);
        long methodElapsed = time - methodBeginTime;

        log("end", methodElapsed);
    }

    private static String getRequestId() {
        return (String) contextTL.get().get(REQUEST_ID);
    }

    private static void log(String method, Long methodElapsed) {
        /*logger.debug("[" + getRequestId() + "] " + getElapsedTime(methodElapsed) + " " + method + " "
                + contextTL.get().get(REQUEST_URI));*/

        long time = (Long) contextTL.get().get(NOW);
        long total = time - (Long) contextTL.get().get(START);
        //long lately = time - (Long) contextTL.get().get(LAST);
        contextTL.get().put(LAST, time);

        String uri = (String) contextTL.get().get(REQUEST_URI);
        if (methodElapsed == null) {
//            logger.debug("{} ms 进入 {}", total, uri);
        } else {
            logger.debug("{} ms {}", methodElapsed, method);
        }
    }

    private static String getElapsedTime(Long methodElapsed) {
        long time = (Long) contextTL.get().get(NOW);
        long total = time - (Long) contextTL.get().get(START);
        long lately = time - (Long) contextTL.get().get(LAST);
        contextTL.get().put(LAST, time);

        return lately + "ms/" + (methodElapsed == null ? "enter/" : methodElapsed + "ms/") + total + "ms";
    }

    @Pointcut("execution(* com.lebo.rest.*Controller.*(..)) || execution(* com.lebo.service.*Service.*(..)) || execution(* com.lebo.repository.*Dao.*(..))")
    private void controllerServiceDaoMethod() {
    }

    @Before("controllerServiceDaoMethod()")
    public void beforeControllerServiceDao(JoinPoint joinPoint) {
        // 计算花费时间时，使用一致的“现在时间”
        long time = System.currentTimeMillis();
        contextTL.get().put(NOW, time);
        // 用于计算在一个方法中花费了多少时间
        contextTL.get().put(joinPoint.getSignature().toLongString() + " begin", time);
        log(joinPoint.getSignature().toShortString(), null);
    }

    @After("controllerServiceDaoMethod()")
    public void afterControllerServiceDao(JoinPoint joinPoint) {
        // 计算花费时间时，使用一致的“现在时间”
        long time = System.currentTimeMillis();
        contextTL.get().put(NOW, time);
        // 方法中花费的时间
        long methodBeginTime = (Long) contextTL.get().get(joinPoint.getSignature().toLongString() + " begin");
        long methodElapsed = time - methodBeginTime;

        log(joinPoint.getSignature().toShortString(), methodElapsed);
    }
}
