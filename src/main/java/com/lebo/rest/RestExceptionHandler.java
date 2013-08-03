package com.lebo.rest;

import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.DuplicateException;
import com.lebo.service.ServiceException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public final ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return ErrorDto.badRequest(NestedExceptionUtils.buildMessage(ex.getMessage(), ex));
    }

    @ExceptionHandler(value = {ServiceException.class})
    public final ResponseEntity<?> handleServiceException(ServiceException ex, WebRequest request) {
        return ErrorDto.badRequest(ex.getMessage());
    }

    @ExceptionHandler(value = {DuplicateException.class})
    public final ResponseEntity<?> handleDuplicateException(DuplicateException ex, WebRequest request) {
        return ErrorDto.duplicate();
    }

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<?> handleAllException(Exception ex, WebRequest request) {
        return ErrorDto.badRequest(NestedExceptionUtils.buildMessage(ex.getMessage(), ex));
    }

    @Override
    protected ResponseEntity handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        switch (status) {
            case BAD_REQUEST:
                return ErrorDto.badRequest(ex);
            case NOT_FOUND:
                return ErrorDto.notFound();
            case INTERNAL_SERVER_ERROR:
                return ErrorDto.internalServerError(ex);
            default:
                return super.handleExceptionInternal(ex, body, headers, status, request);
        }
    }
}