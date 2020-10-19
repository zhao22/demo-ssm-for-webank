package com.seanzx.enums;

/**
 * 正则枚举
 * @author zhaoxin
 * @date 2020/10/19
 */
public enum Regex {

    MOBILE("1[3456789]d{9}"),
    EMAIL("(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)");

    private final String expression;

    Regex(String expression) {
        this.expression = expression;
    }

    public String expr() {
        return expression;
    }
}
