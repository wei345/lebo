package com.lebo.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM7:02
 */
public class FileContentUrlUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static FileStorageService fileStorageService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FileContentUrlUtils.applicationContext = applicationContext;
    }

    public static String getContentUrl(String id, String suffix) {

        if (id == null) {
            return null;
        }

        if (fileStorageService == null) {
            fileStorageService = applicationContext.getBean(FileStorageService.class);
        }

        return fileStorageService.getContentUrl(id, suffix);
    }

    public static String getContentUrl(String id) {
        return getContentUrl(id, null);
    }
}
