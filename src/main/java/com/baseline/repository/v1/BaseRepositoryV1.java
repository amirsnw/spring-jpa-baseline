package com.baseline.repository.v1;

import com.baseline.common.model.SearchResultModel;
import com.baseline.repository.v1.dto.FilterWrapper;

public interface BaseRepositoryV1<ENTITY> {
    SearchResultModel<ENTITY> search(Class<ENTITY> entityClass, FilterWrapper filterWrapper, Integer start, Integer limit);
}
