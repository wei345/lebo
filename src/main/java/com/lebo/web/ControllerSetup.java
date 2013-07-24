package com.lebo.web;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ControllerAdvice
public class ControllerSetup {
    /**
     * 格式：EEE MMM dd HH:mm:ss ZZZZZ yyyy
     * 例如：Mon Jul 22 10:54:31 +0800 2013
     * <p/>
     * 这是Twitter和新浪微博采用的日期格式。
     */
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT;

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        DEFAULT_DATE_FORMAT = dateFormat;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //trim 字符串参数
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

        //日期字符串 -> Date类型
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DEFAULT_DATE_FORMAT, false));
    }

}