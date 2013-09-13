package com.lebo.jmx;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 * 根据classpath获得文件路径.
 *
 * @author: Wei Liu
 * Date: 13-9-13
 * Time: PM11:44
 */
public class FilePathFactoryBean implements FactoryBean<String> {
    private String classpath;

    @Override
    public String getObject() throws Exception {
        return new ClassPathResource(classpath).getFile().getAbsolutePath();
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }
}
