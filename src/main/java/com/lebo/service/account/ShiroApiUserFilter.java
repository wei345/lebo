package com.lebo.service.account;

import com.lebo.rest.dto.ErrorDto;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * 当需要客户端登录时，返回合适的JSON，而不是跳转到登录页面。
 *
 * @author: Wei Liu
 * Date: 13-6-28
 * Time: PM5:23
 */
public class ShiroApiUserFilter extends UserFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        out.write(ErrorDto.UNAUTHORIZED.toJson());
        out.flush();
        return false;
    }
}
