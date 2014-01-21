package com.lebo.service.account;

import com.lebo.rest.dto.ErrorDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author: Wei Liu
 * Date: 14-1-21
 * Time: PM12:26
 */
public class BannedFilter extends AccessControlFilter implements ApplicationContextAware {

    private AccountService accountService;
    private ApplicationContext applicationContext;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

        return !getAccountService().isBanned(user.id);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        ((HttpServletResponse) response).setStatus(401);

        PrintWriter out = response.getWriter();

        out.write(ErrorDto.BANNED.toJson());

        out.flush();

        return false;
    }

    public AccountService getAccountService() {

        if (accountService == null) {
            accountService = applicationContext.getBean(AccountService.class);
        }

        return accountService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
