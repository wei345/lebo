package com.lebo.service.account;

import com.lebo.rest.ErrorDto;
import org.springside.modules.mapper.JsonMapper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: Wei Liu
 * Date: 13-6-28
 * Time: PM7:12
 */
public class WebApiUtils {

    public static void sendUnauthorized(ServletRequest request, ServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        out.write(ErrorDto.UNAUTHORIZED.toJson());
        out.flush();
    }

    public static void sendForbidden(ServletRequest request, ServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        out.write(ErrorDto.FORBIDDEN.toJson());
        out.flush();
    }
}
