package com.seanzx.common;

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

    public void setTotal(long total) {
        this.total = total;
    }

    public void setList(List<Element> list) {
        this.list = list;
    }
}