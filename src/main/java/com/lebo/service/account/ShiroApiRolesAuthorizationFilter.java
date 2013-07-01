package com.lebo.service.account;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-6-28
 * Time: PM7:08
 */
public class ShiroApiRolesAuthorizationFilter extends RolesAuthorizationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
            WebApiUtils.sendUnauthorized(request, response);
        } else {
            // If subject is known but not authorized, redirect to the unauthorized URL if there is one
            // If no unauthorized URL is specified, just return an unauthorized HTTP status code
            WebApiUtils.sendUnauthorized(request, response);
        }
        return false;
    }
}
