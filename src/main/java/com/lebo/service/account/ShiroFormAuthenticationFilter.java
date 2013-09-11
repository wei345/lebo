package com.lebo.service.account;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: Wei Liu
 * Date: 13-7-16
 * Time: PM11:42
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        AbstractShiroLogin.useDbLogin(null, null, ((HttpServletRequest) request).getSession().getServletContext());
        return super.createToken(request, response);
    }

    /**
     * 允许访问登录页面 GET /login
     * 在生产环境shiro loginUrl设为404
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (pathsMatch("/login", request) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(GET_METHOD)) {
            //允许访问登录页
            return true;
        }
        return super.onAccessDenied(request, response);
    }
}
