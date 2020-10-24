package com.seanzx.common.response;

import java.util.List;

/**
 * 分页结果封装
 * @author zhaoxin
 * @date 2019/12/16
 */
public class Page<Element> {
    long total;
    List<Element> list;

    public Page(long total, List<Element> list) {
        this.total = total;
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public List<Element> getList() {
        return list;
    }
}