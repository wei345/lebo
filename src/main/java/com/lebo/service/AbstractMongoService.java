package com.lebo.service;

import com.lebo.repository.MongoConstant;
import com.lebo.service.param.PaginationParam;
import com.lebo.util.ContentTypeMap;
import com.mongodb.MongoException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import org.springside.modules.utils.DateProvider;
import org.springside.modules.utils.Reflections;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM9:01
 */
public abstract class AbstractMongoService {
    @Autowired
    protected MongoTemplate mongoTemplate;
    protected DateProvider dateProvider = DateProvider.DEFAULT;
    protected static Method orOperator = Reflections.getAccessibleMethod(new Criteria(), "orOperator",
            new Class[]{Criteria[].class});
    protected static Method andOperator = Reflections.getAccessibleMethod(new Criteria(), "andOperator",
            new Class[]{Criteria[].class});
    protected static Pattern mongoIdPattern = Pattern.compile("^[0-9a-f]{24}$", Pattern.CASE_INSENSITIVE);

    /**
     * 执行MongoDB的<code>getLastError</code>命令，如果发现错误状态则抛出异常。
     *
     * @throws DuplicateException 当违反唯一约束时
     * @throws MongoException     当其他错误状态时
     * @see com.mongodb.CommandResult#throwOnError()
     */
    protected void throwOnMongoError() {
        try {
            mongoTemplate.executeCommand("{ getLastError : 1 }").throwOnError();
        } catch (MongoException e) {
            switch (e.getCode()) {
                case MongoConstant.MONGO_ERROR_CODE_DUPLICATE_KEY:
                    throw new DuplicateException(e.getMessage(), e);
                default:
                    throw e;
            }
        }
    }

    protected Criteria orOperator(List<Criteria> criterias) {
        try {
            //orOperator是可变参数，只好反射调用。这蛋疼的语法。
            return (Criteria) orOperator.invoke(
                    new Criteria(), new Object[]{criterias.toArray(new Criteria[]{})});
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
    }

    protected Criteria andOperator(List<Criteria> criterias) {
        try {
            //orOperator是可变参数，只好反射调用。这蛋疼的语法。
            return (Criteria) andOperator.invoke(
                    new Criteria(), new Object[]{criterias.toArray(new Criteria[]{})});
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
    }

    public static boolean isMongoId(String str) {
        if (str == null) {
            return false;
        }
        return mongoIdPattern.matcher(str).matches();
    }

    public void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    public static void paginationById(Query query, PaginationParam paginationParam) {
        //分页
        Criteria criteria = new Criteria("_id");
        boolean flag = false;
        if (!paginationParam.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) {
            criteria.lt(new ObjectId(paginationParam.getMaxId()));
            flag = true;
        }
        if (!paginationParam.getSinceId().equals(MongoConstant.MONGO_ID_MIN_VALUE)) {
            criteria.gt(new ObjectId(paginationParam.getSinceId()));
            flag = true;
        }

        if (flag) {
            query.addCriteria(criteria);
        }

        query.with(paginationParam.getSort()).limit(paginationParam.getCount());
    }

    public static String newMongoId(Date time) {
        return new ObjectId(time).toString();
    }

    /**
     * 生成和comment有关联、可读性好的fileId。
     * 返回值格式：
     * <pre>
     * {collectionName}/{commentId}-{length}byte.{ext}
     * </pre>
     */
    public static String generateFileId(String fileCollectionName, String mongoId, long length, String contentType, String filename) {
        Assert.hasText(mongoId);

        String fileId = String.format("%s/%s-%s", fileCollectionName, mongoId, length);

        //扩展名
        String ext = ContentTypeMap.getExtension(contentType, filename);
        if (ext != null) {
            fileId = fileId + "." + ext;
        }

        return fileId;
    }

    public static boolean isFileId(String s) {
        //不是url路径，即认为是文件id
        return !StringUtils.startsWithAny(s.toLowerCase(), "http://", "https://");
    }
}
