package com.lebo.service.param;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:36
 */
public class SearchParam implements Pageable, Serializable {
    private int page;
    private int size;
    private Sort sort;

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

    /*
             * (non-Javadoc)
             *
             * @see org.springframework.data.domain.Pageable#getPageSize()
             */
    @DecimalMax("200")
    @DecimalMin("1")
    public int getPageSize() {

        return size;
    }

    /*
             * (non-Javadoc)
             *
             * @see org.springframework.data.domain.Pageable#getPageNumber()
             */
    @DecimalMax("1000")
    @DecimalMin("0")
    public int getPageNumber() {

        return page;
    }

    /*
             * (non-Javadoc)
             *
             * @see org.springframework.data.domain.Pageable#getFirstItem()
             */
    public int getOffset() {

        return page * size;
    }

    /*
             * (non-Javadoc)
             *
             * @see org.springframework.data.domain.Pageable#getSort()
             */
    public Sort getSort() {

        return sort;
    }

    /*
             * (non-Javadoc)
             *
             * @see java.lang.Object#equals(java.lang.Object)
             */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof SearchParam)) {
            return false;
        }

        SearchParam that = (SearchParam) obj;

        boolean pageEqual = this.page == that.page;
        boolean sizeEqual = this.size == that.size;

        boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);

        return pageEqual && sizeEqual && sortEqual;
    }

    /*
             * (non-Javadoc)
             *
             * @see java.lang.Object#hashCode()
             */
    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + page;
        result = 31 * result + size;
        result = 31 * result + (null == sort ? 0 : sort.hashCode());

        return result;
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

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (0 > page) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }

        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (0 >= size) {
            throw new IllegalArgumentException("Page size must not be less than or equal to zero!");
        }

        this.size = size;
    }

    //TODO 限制size和page
}
