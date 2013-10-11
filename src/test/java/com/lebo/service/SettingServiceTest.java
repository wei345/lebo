package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-9-10
 * Time: PM6:01
 */
public class SettingServiceTest extends SpringContextTestCase {
    @Autowired
    private SettingService settingService;

    @Test
    public void getAllChannels() {
        settingService.getAllChannels();
    }

    @Test
    public void getEnabledChannels() {
        settingService.getEnabledChannels();
    }
}
