package com.lebo.service;

import com.lebo.repository.MongoConstant;
import com.lebo.service.param.PaginationParam;
import com.lebo.util.ContentTypeMap;
import com.mongodb.BasicDBList;
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

import java.text.SimpleDateFormat;
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

            BasicDBList bsonList = createCriteriaList(criterias.toArray(new Criteria[]{}));
            return new Criteria("$or").is(bsonList);

        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
    }

    protected Criteria andOperator(List<Criteria> criterias) {
        try {

            BasicDBList bsonList = createCriteriaList(criterias.toArray(new Criteria[]{}));
            return new Criteria("$and").is(bsonList);

        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 拷贝自org.springframework.data.mongodb.core.query.Criteria#createCriteriaList
     */
    private BasicDBList createCriteriaList(Criteria[] criteria) {
        BasicDBList bsonList = new BasicDBList();
        for (Criteria c : criteria) {
            bsonList.add(c.getCriteriaObject());
        }
        return bsonList;
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

        Criteria criteria = new Criteria("_id");

        boolean add = false;

        if (!paginationParam.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) {
            criteria.lt(new ObjectId(paginationParam.getMaxId()));
            add = true;
        }

        if (!paginationParam.getSinceId().equals(MongoConstant.MONGO_ID_MIN_VALUE)) {
            criteria.gt(new ObjectId(paginationParam.getSinceId()));
            add = true;
        }

        if (add) {
            query.addCriteria(criteria);
        }

        query.with(paginationParam.getSort()).limit(paginationParam.getCount());
    }

    public static String newMongoId(Date time) {
        return new ObjectId(time).toString();
    }


    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 生成和数据库记录有关联、可读性好的fileId。
     * 返回值格式：
     * <pre>
     * {collectionName}/{yyyy-MM-dd}/{mongoId}-{slug}-{length}.{ext}
     * </pre>
     */
    public static String generateFileId(String fileCollectionName, String mongoId, String slug, long length, String contentType, String filename) {
        Assert.hasText(fileCollectionName);

        StringBuilder sb = new StringBuilder(fileCollectionName + "/");

        //日期/mongoId
        if (StringUtils.isNotBlank(mongoId)) {
            String datePath = sdf.format(new ObjectId(mongoId).getTime());
            sb.append(datePath + "/" + mongoId);
        }

        //slug
        if (StringUtils.isNotBlank(slug)) {
            if (sb.toString().endsWith("/")) {
                sb.append(slug);
            } else {
                sb.append("-" + slug);
            }
        }

        //长度,字节
        sb.append("-" + length);

        //扩展名
        String ext = ContentTypeMap.getExtension(contentType, filename);
        if (StringUtils.isNotBlank(ext)) {
            sb.append("." + ext);
        }

        return sb.toString();
    }

    public static boolean isFileId(String s) {
        //不是url路径，即认为是文件id
        return (s != null) && !StringUtils.startsWithAny(s.toLowerCase(), "http://", "https://");
    }
}
