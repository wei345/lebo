package com.lebo.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lebo.service.ServiceException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final int UNAUTHORIZED_CODE = 10403;
    private static final int NOT_FOUND_CODE = 10404;
    public static final ErrorDto BAD_REQUEST = new ErrorDto("Bad Request", 10400, HttpStatus.BAD_REQUEST);
    public static final ErrorDto UNAUTHORIZED = new ErrorDto("Unauthorized", 10401, HttpStatus.UNAUTHORIZED);
    public static final ErrorDto FORBIDDEN = new ErrorDto("Forbidden", UNAUTHORIZED_CODE, HttpStatus.FORBIDDEN);
    public static final ErrorDto NOT_FOUND = new ErrorDto("Not Found", 10404, HttpStatus.NOT_FOUND);
    public static final ErrorDto DUPLICATE = new ErrorDto("Duplicate", 10427, HttpStatus.BAD_REQUEST);
    public static final ErrorDto CAN_NOT_FOLLOW_BECAUSE_BLOCKED = new ErrorDto("不能关注该用户，因为你在对方的黑名单中", 10428, HttpStatus.BAD_REQUEST);
    public static final ErrorDto CAN_NOT_FOLLOW_BECAUSE_BLOCKING = new ErrorDto("不能关注该用户，因为对方在你的黑名单中", 10429, HttpStatus.BAD_REQUEST);
    public static final ErrorDto CAN_NOT_FOLLOW_BECAUSE_TOO_MANY = new ErrorDto("不能关注该用户，因为你关注的人数已达到上限", 10430, HttpStatus.BAD_REQUEST);
    public static final ErrorDto CAN_NOT_FOLLOW_DIGEST_ACCOUNT = new ErrorDto("不能关注乐播精品账号", 10433, HttpStatus.BAD_REQUEST);
    public static final ErrorDto INTERNAL_SERVER_ERROR = new ErrorDto("Internal Server Error", 10500, HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorDto FIND_WEIBO_FRIEND_NO_TOKEN = new ErrorDto("没有微博token", 10431, HttpStatus.BAD_REQUEST);
    public static final ErrorDto FIND_WEIBO_FRIEND_ERROR_TOKEN = new ErrorDto("token不正确或已过期", 10432, HttpStatus.BAD_REQUEST);
    public static final ErrorDto INSUFFICIENT_GOLD = new ErrorDto("金币不足", 10434, HttpStatus.BAD_REQUEST);
    public static final SuccessDto OK = new SuccessDto();

    private static JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
    private String json;
    private HttpStatus httpStatus;

    private Error error;

    // for jackson JsonMapping only
    public ErrorDto() {
    }

    private ErrorDto(String message, int code, HttpStatus httpStatus) {
        error = new Error(message, code);
        this.httpStatus = httpStatus;
    }

    public static ErrorDto newBadRequestError(String message) {
        return new ErrorDto(message, 10400, HttpStatus.BAD_REQUEST);
    }

    private static ErrorDto newInternalServerError(String message) {
        return new ErrorDto(message, 10500, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ErrorDto> badRequest(String message) {
        return new ResponseEntity<ErrorDto>(newBadRequestError(message), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ErrorDto> badRequest(ServiceException e) {
        if (e.getErrorDto() != null) {
            return new ResponseEntity<ErrorDto>(e.getErrorDto(), e.getErrorDto().httpStatus);
        } else {
            return new ResponseEntity<ErrorDto>(newBadRequestError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public static ResponseEntity<ErrorDto> badRequest(Exception e) {
        return new ResponseEntity<ErrorDto>(newBadRequestError(NestedExceptionUtils.buildMessage(e.getMessage(), e.getCause())), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ErrorDto> internalServerError(String message) {
        return new ResponseEntity<ErrorDto>(newInternalServerError(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ErrorDto> internalServerError(Exception e) {
        return new ResponseEntity<ErrorDto>(newInternalServerError(NestedExceptionUtils.buildMessage(e.getMessage(), e.getCause())), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ErrorDto> notFound() {
        return new ResponseEntity<ErrorDto>(ErrorDto.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ErrorDto> notFound(String message) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(message, NOT_FOUND_CODE, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ErrorDto> unauthorized() {
        return new ResponseEntity<ErrorDto>(ErrorDto.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ErrorDto> forbidden() {
        return new ResponseEntity<ErrorDto>(ErrorDto.FORBIDDEN, HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<ErrorDto> unauthorized(String message) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(message, UNAUTHORIZED_CODE, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ErrorDto> duplicate() {
        return new ResponseEntity<ErrorDto>(ErrorDto.DUPLICATE, HttpStatus.BAD_REQUEST);
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

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ResponseEntity<ErrorDto> toResponseEntity() {
        return new ResponseEntity<ErrorDto>(this, httpStatus);
    }

    public static class Error {
        private String message;
        private int code;

        // for jackson JsonMapping only
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

    public static class SuccessDto {
        private boolean ok = true;

        public boolean isOk() {
            return ok;
        }

        public void setOk(boolean ok) {
            this.ok = ok;
        }
    }
}
