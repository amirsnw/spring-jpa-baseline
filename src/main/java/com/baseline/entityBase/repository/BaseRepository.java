package com.baseline.entityBase.repository;

import com.baseline.common.dto.FilterDto;
import com.baseline.common.dto.SearchDto;
import com.baseline.common.enumeration.RaSqlFunction;
import com.baseline.common.model.ChartDataModel;
import com.baseline.common.model.SearchResultModel;

import java.util.List;

public interface BaseRepository<Entity> {
    SearchResultModel<Entity> search(Class<Entity> entityClass, SearchDto searchDTO);

    Entity findFirst(Class<Entity> entityClass, SearchDto searchDTO);

    List<Entity> findAll(Class<Entity> entityClass, SearchDto searchDTO);

    Long count(Class<Entity> entityClass, List<FilterDto> filters);

    Number sqlFunction(Class<Entity> entityClass, List<FilterDto> filters, RaSqlFunction function, String field);

    List<ChartDataModel> groupBy(Class<Entity> entityClass, List<FilterDto> filters, String field, RaSqlFunction function, String functionField);
}
