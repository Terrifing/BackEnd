package org.acme;

public class Page {
    Page(int pageNumber, int pageSize) {
        this.pageSize = pageSize;
        this.offset = pageNumber;
    };
    private int offset;
    private int pageSize;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
