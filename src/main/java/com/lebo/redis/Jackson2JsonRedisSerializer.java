package com.lebo.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * 复制自org.springframework.data.redis.serializer.JacksonJsonRedisSerializer，做了少量修改，使用JacksonJ 2，添加无参构造函数。
 *
 * @author: Wei Liu
 * Date: 14-1-2
 * Time: PM10:53
 */
public class Jackson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static final byte[] EMPTY_ARRAY = new byte[0];

    private final JavaType javaType;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Jackson2JsonRedisSerializer(){
        javaType = TypeFactory.unknownType();
    }

    public Jackson2JsonRedisSerializer(Class<T> type) {
        this.javaType = TypeFactory.defaultInstance().constructFromCanonical(type.getCanonicalName());
    }

    @SuppressWarnings("unchecked")
    public T deserialize(byte[] bytes) throws SerializationException {
        if (isEmpty(bytes)) {
            return null;
        }
        try {
            return (T) this.objectMapper.readValue(bytes, 0, bytes.length, javaType);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }


    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        }
        try {
            return this.objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    /**
     * Sets the {@code ObjectMapper} for this view. If not set, a default
     * {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
     * <p>Setting a custom-configured {@code ObjectMapper} is one way to take further control of the JSON serialization
     * process. For example, an extended {@link com.fasterxml.jackson.databind.ser.SerializerFactory} can be configured that provides
     * custom serializers for specific types. The other option for refining the serialization process is to use Jackson's
     * provided annotations on the types to be serialized, in which case a custom-configured ObjectMapper is unnecessary.
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    /**
     * Returns the Jackson {@link JavaType} for the specific class.
     * <p/>
     * <p>Default implementation returns {@link TypeFactory#constructFromCanonical(java.lang.String)}, but this can be overridden
     * in subclasses, to allow for custom generic collection handling. For instance:
     * <pre class="code">
     * protected JavaType getJavaType(Class&lt;?&gt; clazz) {
     * if (List.class.isAssignableFrom(clazz)) {
     * return TypeFactory.collectionType(ArrayList.class, MyBean.class);
     * } else {
     * return super.getJavaType(clazz);
     * }
     * }
     * </pre>
     *
     * @param clazz the class to return the java type for
     * @return the java type
     */
    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructFromCanonical(clazz.getCanonicalName());
    }

    static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }
}
