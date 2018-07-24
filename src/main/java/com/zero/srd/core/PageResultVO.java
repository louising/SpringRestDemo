package com.zero.srd.core;

public class PageResultVO {
    private Object records; //can be any collection class, e.g. Array, List, Set, Map and so on.
    private PageVO pageVO;

    public PageResultVO() {
    }

    public PageResultVO(PageVO pageVO, Object records) {
        super();
        this.records = records;
        this.pageVO = pageVO;
    }

    public Object getRecords() {
        return records;
    }

    public void setRecords(Object records) {
        this.records = records;
    }

    public PageVO getPageVO() {
        return pageVO;
    }

    public void setPageVO(PageVO pageVO) {
        this.pageVO = pageVO;
    }

}