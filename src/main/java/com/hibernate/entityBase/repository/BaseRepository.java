package com.hibernate.entityBase.repository;

import com.hibernate.common.dto.FilterDto;
import com.hibernate.common.dto.SearchDto;
import com.hibernate.common.enumeration.RaSqlFunction;
import com.hibernate.common.model.ChartDataModel;
import com.hibernate.common.model.SearchResultModel;

import java.util.List;

public interface BaseRepository<Entity> {
    SearchResultModel<Entity> search(Class<Entity> entityClass, SearchDto searchDTO);

    Entity findFirst(Class<Entity> entityClass, SearchDto searchDTO);

    List<Entity> findAll(Class<Entity> entityClass, SearchDto searchDTO);

    Long count(Class<Entity> entityClass, List<FilterDto> filters);

    Number sqlFunction(Class<Entity> entityClass, List<FilterDto> filters, RaSqlFunction function, String field);

    List<ChartDataModel> groupBy(Class<Entity> entityClass, List<FilterDto> filters, String field, RaSqlFunction function, String functionField);
}
