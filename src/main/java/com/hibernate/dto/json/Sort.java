package com.hibernate.dto.json;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

/**
 * 
 * @author s_tayari
 *
 */
public class Sort {

    private Direction direction;
    private String property;

    public Sort() {
    }
    
    public Sort(String json) throws IOException {
        Map<String, String> map = new ObjectMapper().readValue(json, Map.class);
        this.direction = Direction.getValue(map.get("direction"));
        this.property = map.get("property");
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

}
