package com.baseline.config.enumeration;

public enum RaSqlFunction {
    SUM("sum", Integer.class),
    COUNT("count", Integer.class),
    AVG("avg", Integer.class),
    MAX("max", Integer.class),
    MIN("min", Integer.class),
    FLOOR("floor", Integer.class);

    private final String name;
    private final Class<?> returnType;

    RaSqlFunction(String name, Class returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

}