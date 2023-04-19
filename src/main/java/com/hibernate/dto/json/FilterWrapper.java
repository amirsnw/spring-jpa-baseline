package com.hibernate.dto.json;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author s_tayari
 *
 */
public class FilterWrapper {

    private Set<Filter> filters;

    public FilterWrapper() {
    }

    public FilterWrapper(String jsonFilters) throws JsonParseException,
            JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        filters = mapper.readValue(jsonFilters, mapper.getTypeFactory()
                .constructCollectionType(Set.class, Filter.class));

    }

    public Set<Filter> getFilters() {
        return filters;
    }

    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }

}
