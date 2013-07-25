package com.lebo.service.param;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:36
 */
public class SearchParam extends PageRequest {
    private String q;
    private Date after;
    //备用字段
    //private String geocode;    //Example Values: 37.781157,-122.398720,1mi
    //private Date until;

    public SearchParam() {
        super(0, PaginationParam.DEFAULT_COUNT);
    }

    public SearchParam(int page, int size) {
        super(page, size);
    }

    public SearchParam(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public SearchParam(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public SearchParam(String q, int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
        this.q = q;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Date getAfter() {
        return after;
    }

    public void setAfter(Date after) {
        this.after = after;
    }

    //TODO 限制size和page
}
