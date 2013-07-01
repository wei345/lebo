package com.lebo.repository;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;

public class NoClassFieldMappingMongoConverterFactoryBean implements FactoryBean<MappingMongoConverter> {

    private MappingMongoConverter converter;

    public void setConverter(MappingMongoConverter converter) {
        this.converter = converter;
    }

    public MappingMongoConverter getObject() throws Exception {
        MongoTypeMapper typeMapper = new DefaultMongoTypeMapper(null);
        converter.setTypeMapper(typeMapper);
        return converter;
    }

    public Class<?> getObjectType() {
        return MappingMongoConverter.class;
    }

    public boolean isSingleton() {
        return true;
    }

}