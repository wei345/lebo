package com.lebo.service.param;

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
        this(0, PaginationParam.DEFAULT_COUNT);
    }

    public SearchParam(String q, int page, int size, Sort.Direction direction, String... properties) {
        this(page, size, direction, properties);
        this.q = q;
    }

    /**
     * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing 0 for {@code page} will return the first
     * page.
     *
     * @param size
     * @param page
     */
    public SearchParam(int page, int size) {

        this(page, size, null);
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page
     * @param size
     * @param direction
     * @param properties
     */
    public SearchParam(int page, int size, Sort.Direction direction, String... properties) {

        this(page, size, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page
     * @param size
     * @param sort
     */
    public SearchParam(int page, int size, Sort sort) {
        setPage(page);
        setSize(size);
        this.sort = sort;
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
}
