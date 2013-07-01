package com.lebo.rest;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态码命名规则：API版本号 + 3位数字，"3位数字"含义尽量与HTTP状态码对应。
 * 例如：API版本号v1，客户端请求错误，错误码为10400。其中"10"表示版本号1.0，400表示错误的请求（HTTP状态码400）
 * @author: Wei Liu
 * Date: 13-6-27
 * Time: PM6:14
 */
public class  ErrorDto {
    public static final ErrorDto BAD_REQUEST = new ErrorDto("Bad request", 10400);
    public static final ErrorDto UNAUTHORIZED = new ErrorDto("Unauthorized", 10401);
    public static final ErrorDto NOT_FOUND = new ErrorDto("Not found", 10404);
    public static final ErrorDto UNKNOWN_ERROR = new ErrorDto("Unknown error", 10500);

    private Map<String, Object> error = new HashMap<String, Object>();

    // for jackson JsonMapping
    public ErrorDto(){}

    public ErrorDto(String message){
        error.put("message", message);
    }

    private ErrorDto(String message, int code){
        error.put("message", message);
        error.put("code", code);
    }

    public Map<String, Object> getError() {
        return error;
    }
}
