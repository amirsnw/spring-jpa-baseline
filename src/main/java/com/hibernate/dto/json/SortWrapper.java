package com.hibernate.dto.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;

/**
 * 
 * @author s_tayari
 *
 */
public class SortWrapper {

    private Set<Sort> sortSet;
    
    public SortWrapper() {
    }

    public SortWrapper(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        sortSet = mapper.readValue(json, mapper.getTypeFactory()
                .constructCollectionType(Set.class, Sort.class));
    }

    public Set<Sort> getSortSet() {
        return sortSet;
    }

    public void setSortSet(Set<Sort> sortSet) {
        this.sortSet = sortSet;
    }
    
}
