package com.lebo.filter;

import com.lebo.entity.User;
import com.lebo.redis.RedisKeys;
import com.lebo.service.account.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springside.modules.nosql.redis.JedisTemplate;
import redis.clients.jedis.Jedis;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 记录用户活跃天数.
 *
 * @author: Wei Liu
 * Date: 13-12-20
 * Time: PM4:24
 */
public class UserActiveDaysFilter extends OncePerRequestFilter {

    private JedisTemplate jedisTemplate;

    private MongoTemplate mongoTemplate;

    private int timeToLiveSeconds = 60 * 60 * 24;

    private Logger logger = LoggerFactory.getLogger(UserActiveDaysFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        getJedisTemplate(request);
        getMongoTemplate(request);

        incActiveDays(request);

        filterChain.doFilter(request, response);
    }


    private void incActiveDays(HttpServletRequest request) {

        long beginTime = System.currentTimeMillis();

        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

        if (user == null) {
            return; //未登录
        }

        final String key = RedisKeys.getActiveUserKey();

        //增加活跃天数
        if (signIn(key, user.id, request)) {

            mongoTemplate.updateFirst(
                    new Query(new Criteria(User.ID_KEY).is(user.id)),
                    new Update().inc(User.ACTIVE_DAYS_KEY, 1),
                    User.class);

            logger.debug("{} 活跃天数+1", user.screenName);
        }

        logger.debug("{} ms", System.currentTimeMillis() - beginTime);
    }


    private JedisTemplate getJedisTemplate(HttpServletRequest request) {

        if (jedisTemplate == null) {
            jedisTemplate = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean(JedisTemplate.class);
        }

        return jedisTemplate;
    }


    private MongoTemplate getMongoTemplate(HttpServletRequest request) {

        if (mongoTemplate == null) {
            mongoTemplate = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean(MongoTemplate.class);
        }

        return mongoTemplate;
    }


    /**
     * 签到.
     *
     * @return 签到成功返回true, 已签到返回false
     */
    private boolean signIn(final String key, final String userId, final HttpServletRequest request) {

        if (request.getSession().getAttribute(key) != null) {
            return false; //已签到
        }

        //下次从session中读取，0 ms，减少redis访问，省 1 ms
        request.getSession().setAttribute(key, true);

        //签到
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {

                if (jedis.sadd(key, userId) == 1) { //签到成功

                    jedis.expire(key, timeToLiveSeconds);

                    return true;
                }

                return false; //已签到
            }
        });
    }
}
