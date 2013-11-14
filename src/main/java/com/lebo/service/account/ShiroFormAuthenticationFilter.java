package com.lebo.service.account;

import org.apache.shiro.authc.AuthenticationException;
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

}
