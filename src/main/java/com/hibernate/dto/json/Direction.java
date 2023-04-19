package com.hibernate.dto.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Direction {

    ASC("asc"),
    DESC("desc");

    private String type;

    Direction(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @JsonCreator
    public Direction fromString(String value) {
        return getValue(value);
    }

    @JsonValue
    public String fromEnum(Direction direction) {
        return direction.type;
    }

    public static Direction getValue(String value) {
        for (Direction direction : Direction.values()) {
            if (direction.type.equalsIgnoreCase(value)) {
                return direction;
            }
        }
        return null;
    }
}
