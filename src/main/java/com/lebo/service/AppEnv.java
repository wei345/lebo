package com.lebo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: Wei Liu
 * Date: 13-11-2
 * Time: PM3:53
 */
@Service
public class AppEnv {
    @Value("${server.node_name}")
    private String nodeName;

    @Value("${app.baseurl}")
    private String baseurl;

    public boolean isDevelopment() {
        return "default".equals(nodeName) || "dev".equals(nodeName);
    }

    public String getBaseurl() {
        return baseurl;
    }
}
