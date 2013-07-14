package com.lebo.service;

/**
 * @author: Wei Liu
 * Date: 13-7-5
 * Time: PM6:18
 */
public class DuplicateException extends ServiceException {

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
