package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-11-7
 * Time: AM10:41
 */
public class UploadServiceTest extends SpringContextTestCase {

    @Autowired
    private UploadService uploadService;

    @Test
    public void generateTmpUploadPath() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 10, 7, 6, 59, 50);
        String path = uploadService.generateTmpUploadPath(calendar.getTime(), "image/png", "image-png");
        assertTrue(path.matches("^tmp/expire-2013-11-07-06-59-50-image-png-[0-9a-e]{24}\\.png$"));
    }

    @Test
    public void clearTmp() {
        Calendar calendar = Calendar.getInstance();
        uploadService.newTmpUploadUrl(calendar.getTime(), "video/mp4", "video-mp4");
        uploadService.cleanExpireUrl();
    }

    @Test
    public void newTmpUploadUrl() {
        uploadService.newTmpUploadUrl(new Date(), "video/mp4", "video-mp4");
    }
}
