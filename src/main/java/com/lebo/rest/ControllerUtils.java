package com.lebo.rest;

import com.lebo.service.account.ShiroUser;
import org.apache.shiro.SecurityUtils;

/**
 * @author: Wei Liu
 * Date: 13-7-5
 * Time: PM1:34
 */
public class ControllerUtils {
    public static String getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
