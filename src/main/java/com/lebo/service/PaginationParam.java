package com.lebo.service;

import com.lebo.repository.MongoConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;

/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: PM4:46
 */
public class PaginationParam implements Pageable, Serializable {
    public static final int DEFAULT_COUNT = 20;

    private int count = DEFAULT_COUNT;
    private String maxId;
    private String sinceId;
    private Sort sort;

    public static final Sort DEFAULT_SORT = new Sort(new Sort.Order(Sort.Direction.DESC, "_id"));

    /**
     * 当maxId和sinceId都为空时，返回true，否则返回false
     */
    public boolean canIgnoreIdCondition(){
        return (StringUtils.isBlank(maxId) && StringUtils.isBlank(sinceId));
    }

    @DecimalMax("200")
    @DecimalMin("1")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 当没有指定sinceId时，返回"ffffffffffffffffffffffff"，否则返回真实的Mongo ID。
     * 实际上，在查询第1页时没有maxId，从第2页开始总是有maxId。
     */
    public String getMaxId() {
        return (StringUtils.isBlank(maxId) ? MongoConstant.MONGO_ID_MAX_VALUE : maxId);
    }

    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

    /**
     * 当没有指定sinceId时，返回"000000000000000000000000"，否则返回真实的Mongo ID。
     */
    public String getSinceId() {
        return (StringUtils.isBlank(sinceId) ? MongoConstant.MONGO_ID_MIN_VALUE : sinceId);
    }

    public void setSinceId(String sinceId) {
        this.sinceId = sinceId;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return getCount();
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public Sort getSort() {
        return (sort == null ? DEFAULT_SORT : sort);
    }
}
