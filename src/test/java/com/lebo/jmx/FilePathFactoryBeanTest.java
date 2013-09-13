package com.lebo.jmx;

import com.lebo.SpringContextTestCase;
import org.junit.Test;

/**
 * @author: Wei Liu
 * Date: 13-9-13
 * Time: PM11:56
 */
public class FilePathFactoryBeanTest extends SpringContextTestCase {
    @Test
    public void getObject() throws Exception {
        FilePathFactoryBean filePathFactoryBean = new FilePathFactoryBean();
        filePathFactoryBean.setClasspath("jmx/jmxremote.access");
        System.out.println(filePathFactoryBean.getObject());
    }
}
