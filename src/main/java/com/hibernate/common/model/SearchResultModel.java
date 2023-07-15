package com.hibernate.common.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchResultModel<E> {
    private List<E> result;

    private Long total;

    private Map<String, Object> metadata;

    public SearchResultModel(List<E> result, Long total) {
        this.result = result;
        this.total = total;
    }
}
