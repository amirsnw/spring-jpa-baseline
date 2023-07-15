package com.hibernate.common.enumeration;

public enum OperatorType {

    EQ, NE, GE, BETWEEN, GT, ILIKE, LE, LT, LIKE, IN, NOT_IN, IS_NULL, IS_NOT_NULL, PATTERN;

    private OperatorType() {
    }
}