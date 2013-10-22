package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.FileInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author: Wei Liu
 * Date: 13-10-21
 * Time: PM4:10
 */
public class ALiYunStorageServiceTest extends SpringContextTestCase {
    @Autowired
    private ALiYunStorageService aLiYunStorageService;

    @Test
    public void getKeyFromUrl() throws Exception {
        assertEquals("tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4",
                aLiYunStorageService.getKeyFromUrl("http://file.dev.lebooo.com/tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1382339078&Signature=KIKelClIZd9J7MiuvcJkkEpyVds%3D"));
    }

    @Test
    public void getMetadata() {
        FileInfo metadata = aLiYunStorageService.getMetadata("post/2013-10-21/5264dabf0cf2d42a7c6601f6-video-2119780.mp4");
        assertEquals(Long.valueOf(2119780), metadata.getLength());

        metadata = aLiYunStorageService.getMetadata("tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4");
        assertNull(metadata);
    }
}
