package com.erabbit.common.entity.web;

/**
 * Describe: 分 页 参 数 封 装
 * */

public class PageDomain {

    /**
     * 当前页
     * */
    private Integer page;

    /**
     * 每页数量
     * */
    private Integer limit;

    /**
     * 获取开始的数据行
     * */
    public Integer start(){
        return (this.page-1)*this.limit;
    }

    /**
     * 获取结束的数据行
     * */
    public Integer end(){
        return this.page*this.limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
