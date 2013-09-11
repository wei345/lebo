package com.lebo.service.account;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 未登录用户跳转到404页面。
 * 因为登录页面有方便的快速登录功能，不应该被非开发人员看到.
 *
 * @author: Wei Liu
 * Date: 13-9-11
 * Time: AM11:34
 */
public class ShiroUserFilter extends UserFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        saveRequest(request);
        WebUtils.issueRedirect(request, response, "/404");
        return false;
    }
}
