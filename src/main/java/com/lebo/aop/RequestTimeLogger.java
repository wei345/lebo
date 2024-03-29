package com.lebo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 记录处理每个请求总共花费的时间和在每个方法里花费的时间。
 *
 * @author liuwei
 */
@SuppressWarnings("unused")
@Component
@Aspect
public class RequestTimeLogger {
    private static Logger logger = LoggerFactory.getLogger(RequestTimeLogger.class);
    private static String START = "start";
    private static String LAST = "last";
    private static String REQUEST_URI = "requestUri";
    private static Random r = new Random();

    private static ThreadLocal<Map<String, Object>> contextTL = new ThreadLocal<Map<String, Object>>() {
        @Override
        public Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static void begin(HttpServletRequest request) {
        contextTL.get().put(REQUEST_URI, request.getRequestURI());

        long time = System.currentTimeMillis();
        contextTL.get().put(START, time);
        contextTL.get().put(LAST, time);

        logger.debug("    begin  {}", request.getRequestURI());
    }

    public static void end() {
        logger.debug(String.format("%6s ms       end       %s", System.currentTimeMillis() - (Long) contextTL.get().get(START),
                contextTL.get().get(REQUEST_URI)));

        contextTL.get().clear();
    }

    public boolean isEnable() {
        return contextTL.get().get(START) != null;
    }

    @Pointcut("execution(* com.lebo.rest.*Controller.*(..)) || execution(* com.lebo.rest.*.*Controller.*(..)) || execution(* com.lebo.service.*Service.*(..)) || execution(* com.lebo.service.*.*Service.*(..)) || execution(* com.lebo.repository.*Dao.*(..))")
    private void controllerServiceDaoMethod() {
    }

    @Before("controllerServiceDaoMethod()")
    public void beforeControllerServiceDao(JoinPoint joinPoint) {
        if (isEnable()) {
            // 用于计算在一个方法中花费了多少时间
            long time = System.currentTimeMillis();
            contextTL.get().put(joinPoint.getSignature().toLongString(), time);
            // 记录最后时间点
            contextTL.get().put(LAST, time);

            /*可随时开启打印进入方法的时间
            logger.debug(String.format("%6s ms %9s  -->  %s", time - (Long) contextTL.get().get(START),
                    "", joinPoint.getSignature().toShortString()));*/
        }
    }

    @After("controllerServiceDaoMethod()")
    public void afterControllerServiceDao(JoinPoint joinPoint) {
        if (isEnable()) {
            // 方法中花费的时间
            long time = System.currentTimeMillis();
            long methodBeginTime = (Long) contextTL.get().get(joinPoint.getSignature().toLongString());
            long methodElapsed = time - methodBeginTime;
            // 记录最后时间点
            contextTL.get().put(LAST, time);

            logger.debug(String.format("%6s ms %6s ms  <--  %s", time - (Long) contextTL.get().get(START),
                    methodElapsed, joinPoint.getSignature().toShortString()));
        }
    }
}
