package com.lebo.web.filter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-8-5
 * Time: PM12:31
 */
public class RequestResponseLoggingFilter extends AbstractRequestLoggingFilter {
    private Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

        super.doFilterInternal(request, responseCopier, filterChain);

        try {
            responseCopier.flushBuffer();
        } finally {
            byte[] copy = responseCopier.getCopy();
            String responseText = new String(copy, response.getCharacterEncoding());
            logger.info("responseText length: {}", responseText.length());
            logger.info("responseText: {}", responseText);
        }
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info("{} {}", request.getMethod(), request.getRequestURL().toString());
        try {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                logger.info("RequestBody: {}", IOUtils.toString(request.getInputStream()));
            }
        } catch (IOException e) {
            logger.warn("打印RequestBody时发生异常", e);
        }

        for (Cookie cookie : request.getCookies()) {
            logger.info("Cookie: {} = {}", cookie.getName(), cookie.getValue());
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {

    }
}
