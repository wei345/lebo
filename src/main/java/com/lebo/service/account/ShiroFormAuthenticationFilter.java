package com.lebo.service.account;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.nosql.redis.JedisTemplate;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Wei Liu
 * Date: 13-7-16
 * Time: PM11:42
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
    @Autowired
    private JedisTemplate jedisTemplate;

    private static final String LOGIN_COUNTER_PREFIX = "login.tries";

    private static final int ONE_HOUR_SECONDS = 3600;

    private static int MAX_LOGIN_RETRY_TIMES = 6;

    private Logger logger = LoggerFactory.getLogger(ShiroFormAuthenticationFilter.class);

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        AbstractShiroLogin.useDbLogin(null, null, ((HttpServletRequest) request).getSession().getServletContext());
        return super.createToken(request, response);
    }

    /**
     * 当登录失败时，记录失败次数.
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, final ServletRequest request, ServletResponse response) {

        int count = jedisTemplate.execute(new JedisTemplate.JedisAction<Integer>() {
            @Override
            public Integer action(Jedis jedis) {
                String key = getLoginCounterKey(request);
                Long v = jedis.incr(key);
                jedis.expire(key, ONE_HOUR_SECONDS);
                return v.intValue();
            }
        });

        logger.debug("{}  {}:{}  登录失败 {} 次", request.getRemoteHost(), getUsername(request), getPassword(request), count);

        return super.onLoginFailure(token, e, request, response);
    }

    /**
     * 登录前检查失败次数，如果超过一定次数则拒绝IP登录尝试
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            int count = NumberUtils.toInt(jedisTemplate.get(getLoginCounterKey(request)), 0);
            if (count >= MAX_LOGIN_RETRY_TIMES) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
                return false;
            }
        }

        return super.onAccessDenied(request, response);
    }

    public static String getLoginCounterKey(ServletRequest request) {
        return LOGIN_COUNTER_PREFIX + ":" + request.getRemoteHost();
    }

}
