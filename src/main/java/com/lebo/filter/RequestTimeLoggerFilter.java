package com.lebo.filter;

import com.lebo.aop.RequestTimeLogger;
import org.apache.shiro.web.servlet.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-9-22
 * Time: PM1:44
 */
public class RequestTimeLoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        RequestTimeLogger.begin((HttpServletRequest) request);
        chain.doFilter(request, response);
        RequestTimeLogger.end();
    }

}
