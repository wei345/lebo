package com.lebo.event.listener;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author: Wei Liu
 * Date: 13-9-2
 * Time: PM6:37
 */
public class UserLevelRecorderTest extends SpringContextTestCase {
    @Autowired
    private UserLevelRecorder userLevelRecorder;
    @Test
    public void getLevel() throws Exception {
        assertTrue(userLevelRecorder.getLevel(0) == 0);
        assertTrue(userLevelRecorder.getLevel(1) == 1);
        assertTrue(userLevelRecorder.getLevel(3) == 2);
        assertTrue(userLevelRecorder.getLevel(4) == 2);
        assertTrue(userLevelRecorder.getLevel(6) == 3);
        assertTrue(userLevelRecorder.getLevel(1829) == 59);
        assertTrue(userLevelRecorder.getLevel(1830) == 60);
        assertTrue(userLevelRecorder.getLevel(1831) == 60);
        assertTrue(userLevelRecorder.getLevel(10000) == 60);
    }
}
