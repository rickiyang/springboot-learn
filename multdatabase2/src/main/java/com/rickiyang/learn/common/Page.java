package com.rickiyang.learn.common;

import java.util.List;

/**
 * @param <T>
 * @ClassName: Page
 * @Description: 分页工具类
 */
public class Page<T> {

    /**
     * 当前页号。
     */
    private int currentPageNo = 1;

    /**
     * 每页数据条数。
     */
    private int pageSize = 20;

    /**
     * 数据总条数。
     */
    private int totalCount;

    /**
     * 数据列表。
     */
    private List<T> dataList;

    public Page() {
    }

    public Page(Integer pageSize) {
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
    }

    public Page(Integer currentPageNo, Integer pageSize) {
        if (currentPageNo != null) {
            this.currentPageNo = currentPageNo;
        }
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
    }

    /**
     * 获取总页数。
     *
     * @return 总页数。
     */
    public int getTotalPageCount() {
        if (pageSize == 0) {
            return 0;
        }
        return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

    /**
     * 获取当前页号。
     *
     * @return 当前页号。
     */
    public int getCurrentPageNo() {
        if (currentPageNo == 0) {
            currentPageNo = 1;
        }
        return currentPageNo;
    }

    /**
     * 设置当前页号。
     *
     * @return 当前页号。
     */
    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    /**
     * 获取开始索引。
     *
     * @return
     */
    public int getStartIndex() {
        return (getCurrentPageNo() - 1) * this.pageSize;
    }

    /**
     * 获取总数据条数。
     *
     * @return 总数据条数。
     */
    public int getTotalCount() {
        return this.totalCount;
    }

    /**
     * 获取每页数据条数。
     *
     * @return 每页数据条数。
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页数据条数。
     *
     * @param pageSize 每页数据条数。
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取数据列表。
     *
     * @return 数据列表。
     */
    public List<T> getDataList() {
        return this.dataList;
    }

    /**
     * 设置数据列表。
     *
     * @param dataList
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * 设置数据总条数。
     *
     * @param totalCount 数据总条数。
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
