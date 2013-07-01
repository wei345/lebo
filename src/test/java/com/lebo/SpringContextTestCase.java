package com.lebo;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author: Wei Liu
 * Date: 13-6-24
 * Time: PM3:55
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("development")
public class SpringContextTestCase extends AbstractJUnit4SpringContextTests {
}
