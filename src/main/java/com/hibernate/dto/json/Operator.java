package com.hibernate.dto.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {
    EQUAL("eq"), NOT_EQUAL("neq"), LIKE("like"), BETWEEN("between"), AFTER(
            "after"), BEFORE("before"), IN("in");

    private String name;

    // @JsonCreator
    Operator(String name) {
        this.name = name;
    }

    @JsonCreator
    public Operator nameToOperator(@JsonProperty("name") String name) {
        return getValue(name);
    }

    @JsonValue
    public String operatorToName(Operator operator) {
        return operator.name;
    }

    public static Operator getValue(String value) {
        for (Operator operator : Operator.values()) {
            if (operator.name.equalsIgnoreCase(value))
                return operator;
        }
        return null;
    }
}
