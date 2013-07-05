package com.lebo.rest;

import org.springside.modules.mapper.JsonMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态码命名规则：API版本号 + 3位数字，"3位数字"含义尽量与HTTP状态码对应。
 * 例如：API版本号v1，客户端请求错误，错误码为10400。其中"10"表示版本号1.0，400表示错误的请求（HTTP状态码400）
 *
 * 不要在此类外部设置状态码。
 *
 * @author: Wei Liu
 * Date: 13-6-27
 * Time: PM6:14
 */
public class  ErrorDto {
    public static final ErrorDto BAD_REQUEST = new ErrorDto("Bad Request", 10400);
    public static final ErrorDto UNAUTHORIZED = new ErrorDto("Unauthorized", 10401);
    public static final ErrorDto FORBIDDEN = new ErrorDto("Forbidden", 10403);
    public static final ErrorDto NOT_FOUND = new ErrorDto("Not Found", 10404);
    public static final ErrorDto DUPLICATE = new ErrorDto("Duplicate", 10427);
    public static final ErrorDto INTERNAL_SERVER_ERROR = new ErrorDto("Internal Server Error", 10500);

    private static JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
    private String json;

    private Error error;

    // default constructor for jackson JsonMapping
    public ErrorDto(){}

    public ErrorDto(String message){
        error = new Error(message, 10500);
    }

    private ErrorDto(String message, int code){
        error = new Error(message, code);
    }

    public Error getError() {
        return error;
    }

    public String toJson() {
        if(json == null){
            json = jsonMapper.toJson(this);
        }
        return json;
    }

    public static class Error{
        private String message;
        private int code;

        // default constructor for jackson JsonMapping
        public Error(){}

        private Error(String message, int code){
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }
    }
}
