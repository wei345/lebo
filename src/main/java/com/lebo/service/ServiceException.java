package com.lebo.service;

import org.springframework.core.NestedRuntimeException;

/**
 * Service层公用的Exception.
 * <p/>
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 *
 * @author Wei Liu
 */
public class ServiceException extends NestedRuntimeException {

    private static final long serialVersionUID = 3583566093089790852L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
