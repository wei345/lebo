package com.lebo.repository;

/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: PM10:20
 */
public class MongoConstant {
    public static final String MONGO_ID_MAX_VALUE = "ffffffffffffffffffffffff";
    public static final String MONGO_ID_MIN_VALUE = "000000000000000000000000";
    public static final String MONGO_ID_DEFAULT_KEY = "_id";

    //所有状态码 http://www.mongodb.org/about/contributors/error-codes/
    public static final int MONGO_ERROR_CODE_DUPLICATE_KEY = 11000;
}
