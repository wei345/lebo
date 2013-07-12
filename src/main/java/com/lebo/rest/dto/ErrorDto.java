package com.lebo.rest.dto;

import org.springside.modules.mapper.JsonMapper;

/**
 * <p>
 * ErrorDto状态码与HTTP状态码对应。
 * 会根据需要增加HTTP没有的状态码。
 * </p>
 * <p>
 * 不要在此类外部设置状态码。
 * </p>
 *
 * @author: Wei Liu
 * Date: 13-6-27
 * Time: PM6:14
 */
public class ErrorDto {
    public static final ErrorDto BAD_REQUEST = new ErrorDto("Bad Request", 400);
    public static final ErrorDto UNAUTHORIZED = new ErrorDto("Unauthorized", 401);
    public static final ErrorDto FORBIDDEN = new ErrorDto("Forbidden", 403);
    public static final ErrorDto NOT_FOUND = new ErrorDto("Not Found", 404);
    public static final ErrorDto DUPLICATE = new ErrorDto("Duplicate", 427);
    public static final ErrorDto INTERNAL_SERVER_ERROR = new ErrorDto("Internal Server Error", 500);

    private static JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
    private String json;

    private Error error;

    // default constructor for jackson JsonMapping
    public ErrorDto() {
    }

    public static ErrorDto newBadRequestError(String message) {
        return new ErrorDto(message, 400);
    }

    public static ErrorDto newInternalServerError(String message) {
        return new ErrorDto(message, 500);
    }

    private ErrorDto(String message, int code) {
        error = new Error(message, code);
    }

    public Error getError() {
        return error;
    }

    public String toJson() {
        if (json == null) {
            json = jsonMapper.toJson(this);
        }
        return json;
    }

    public static class Error {
        private String message;
        private int code;

        // default constructor for jackson JsonMapping
        public Error() {
        }

        private Error(String message, int code) {
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
