package com.lebo.service.account;

import com.lebo.rest.dto.ErrorDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author: Wei Liu
 * Date: 13-12-18
 * Time: PM1:51
 */
public class GuestFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request;

        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

        if (user != null
                && user.isGuest()
                && !"GET".equals(req.getMethod())) {

            //禁止游客访问非GET方法
            return false;
        }

        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        ((HttpServletResponse) response).setStatus(403);

        PrintWriter out = response.getWriter();

        out.write(ErrorDto.FORBIDDEN.toJson());

        out.flush();

        return false;
    }
}
