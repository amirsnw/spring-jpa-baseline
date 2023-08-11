package com.baseline.repository.v1.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FilterWrapper {

    private final Set<Filter> filters;

    private FilterWrapper(Set<Filter> filters) {
        this.filters = filters;
    }

    public FilterWrapper() {
        filters = new HashSet<>();
    }

    public FilterWrapper(String jsonFilters) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        filters = mapper.readValue(jsonFilters, mapper.getTypeFactory()
                .constructCollectionType(Set.class, Filter.class));
    }

    public static FilterWrapperBuilder getBuilder() {
        return new FilterWrapperBuilder();
    }

    public Set<Filter> getFilters() {
        return Collections.unmodifiableSet(filters);
    }

    public static class FilterWrapperBuilder {

        private final Set<Filter> filters = new HashSet<>();

        public FilterWrapperBuilder addFilter(Filter filter) {
            filters.add(filter);
            return this;
        }

        public FilterWrapper build() {
            return new FilterWrapper(filters);
        }
    }
}
