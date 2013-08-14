package com.lebo.service.param;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM6:59
 */
public class PageRequest implements Pageable, Serializable {
    public static final int PAGE_SIZE_MAX = 200;
    public static final int PAGE_SIZE_MIN = 1;
    public static final int PAGE_NUBMER_MAX = 1000;
    public static final int PAGE_NUBMER_START = 0;

    protected int page;
    protected int size = PaginationParam.DEFAULT_COUNT;
    protected Sort sort;

    public PageRequest() {

    }

    public PageRequest(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    /*
         * (non-Javadoc)
         *
         * @see org.springframework.data.domain.Pageable#getPageSize()
         */
    @DecimalMax(PAGE_SIZE_MAX + "")
    @DecimalMin(PAGE_SIZE_MIN + "")
    public int getPageSize() {

        return size;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Pageable#getPageNumber()
     */
    @DecimalMax(PAGE_NUBMER_MAX + "")
    @DecimalMin(PAGE_NUBMER_START + "")
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
}
