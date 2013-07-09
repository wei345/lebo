package com.lebo.service.account;

import com.lebo.rest.dto.ErrorDto;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户访问API，已登录但权限不足，返回JSON。
 *
 * @author: Wei Liu
 * Date: 13-6-28
 * Time: PM7:08
 */
public class ShiroApiRolesAuthorizationFilter extends RolesAuthorizationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        PrintWriter out = response.getWriter();

        // If the subject isn't identified, return an unauthorized error
        if (subject.getPrincipal() == null) {
            out.write(ErrorDto.UNAUTHORIZED.toJson());
        } else {
            // If subject is known but not authorized, return an forbidden error
            out.write(ErrorDto.FORBIDDEN.toJson());
        }

        out.flush();
        return false;
    }
}
