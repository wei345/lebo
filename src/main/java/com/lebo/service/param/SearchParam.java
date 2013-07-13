package com.lebo.service.param;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:36
 */
public class SearchParam extends PaginationParam {
    private String q;
    private String geocode;    //Example Values: 37.781157,-122.398720,1mi

    //private Date until;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }
}
