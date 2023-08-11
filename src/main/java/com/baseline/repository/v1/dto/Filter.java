package com.baseline.repository.v1.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class Filter {

    private String property;
    private Operator operator;
    private String value;

    public Filter(String property, Operator operator) {
        this.property = property;
        this.operator = operator;
    }

    @SuppressWarnings("unchecked")
    public Filter(String json) throws IOException {
        Map<String, String> filter =
                new ObjectMapper().readValue(json, Map.class);

        this.property = filter.get("property");
        this.value = filter.get("value");
        this.operator = Operator.getValue(filter.get("operator"));
    }
}