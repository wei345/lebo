package com.lebo.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 用于乐播客户端解析DTO的JsonMapper.
 *
 * @author: Wei Liu
 * Date: 13-8-23
 * Time: PM5:58
 */
public class JsonMapper {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH));
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) throws IOException {
        return mapper.readValue(jsonString, clazz);
    }

    public static <T> T fromJson(String jsonString, Class<?> collectionClass, Class<?> elementClass) throws IOException {
        return mapper.readValue(jsonString, mapper.getTypeFactory().constructParametricType(collectionClass, elementClass));
    }

}
