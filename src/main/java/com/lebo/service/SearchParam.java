package com.lebo.service;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:36
 */
public class SearchParam extends PaginationParam {
    private String q;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
