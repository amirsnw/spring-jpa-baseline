package com.baseline.service;


import com.baseline.common.dto.FilterDto;
import com.baseline.common.dto.OrderByDto;
import com.baseline.common.dto.SearchDto;
import com.baseline.common.model.ChartDataModel;
import com.baseline.common.model.SearchResultModel;
import com.baseline.config.enumeration.OperatorType;
import com.baseline.config.enumeration.PreFilterType;
import com.baseline.config.enumeration.RaSqlFunction;
import com.baseline.entity.BaseEntity;
import com.baseline.exception.CustomException;
import com.baseline.repository.BaseRepository;
import jakarta.persistence.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public abstract class BaseService<Entity
        extends BaseEntity, Repository extends JpaRepository<Entity, Long> & BaseRepository<Entity>> {

    protected final Logger log = LoggerFactory.getLogger(this.getEntityClass());

    protected final String entityName;

    @Autowired
    protected Repository repository;

    protected BaseService() {
        int pos = this.getEntityClass().getSimpleName().lastIndexOf("Entity");
        this.entityName = this.getEntityClass().getSimpleName().substring(0, pos);
    }

    @Transactional
    public Entity save(Entity entity) {
        boolean isCreate = entity.getId() == null;
        this.preSave(entity, isCreate);
        this.repository.save(entity);
        this.innerPostSave(entity, isCreate);
        this.postSave(entity, isCreate);
        return entity;
    }

    private void innerPostSave(Entity entity, boolean isCreate) {
    }

    private List<Field> getAllFields(List<Field> fields,
                                     Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            this.getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    @Transactional
    public List<Entity> save(List<Entity> entities) {
        this.log.debug("Request to save list of Entities : {}", entities);
        entities.forEach((entityx) -> {
            if (this.findOne(entityx.getId()) == null) {
                throw new CustomException("not.found.entity");
            }
        });
        Iterator iter = entities.iterator();

        while (iter.hasNext()) {
            Entity entity = (Entity) iter.next();
            this.save(entity);
        }

        return entities;
    }

    protected void preSave(Entity entity,
                           boolean isCreate) {
    }

    protected void postSave(Entity entity, boolean isCreate) {
    }

    @Transactional(readOnly = true)
    public Entity findOne(Long id) {
        this.log.debug("Request to get Entity : {}", id);
        this.preLoad(id);
        Entity entity = this.repository.findById(id).orElse(null);
        this.postLoad(entity);
        return entity;
    }

    protected void preLoad(Long id) {
    }

    protected void postLoad(Entity entity) {
        if (entity == null || entity.isDeleted()) {
            throw new CustomException("not.found.entity");
        }
    }

    @Transactional
    public void delete(Long id, boolean isForeHardDelete) {
        Entity entity = this.repository.findById(id).orElseThrow(() -> {
            return new CustomException("not.found.entity");
        });
        if (!isForeHardDelete) {
            this.softDelete(entity);
        } else {
            this.hardDelete(entity);
        }
    }

    protected void softDelete(Entity entity) {
        this.log.debug("Request to soft delete Entity : {}", entity.getId());
        this.preDelete(entity, false);
        entity.setDeleted(true);
        this.renameUniqueField(entity);
        this.repository.save(entity);
        this.postDelete(entity, false);
    }

    protected void renameUniqueField(Entity entity) {
        Field[] declaredFields = entity.getClass().getDeclaredFields();

        for (int i = 0; i < declaredFields.length; ++i) {
            Field field = declaredFields[i];
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation.unique()) {
                    try {
                        field.setAccessible(true);
                        String oldName = (String) field.get(entity);
                        StringBuilder newName = new StringBuilder();
                        newName.append("<DEL>").append(oldName);
                        int maxLength = columnAnnotation.length();
                        String id = String.valueOf(entity.getId());
                        if (id.length() >= maxLength) {
                            field.set(entity, id.substring(0, maxLength));
                        } else {
                            if (newName.length() + id.length() + 1 > maxLength) {
                                int emptyLength = maxLength - newName.length();
                                int needSpace = id.length() + 1 - emptyLength;
                                newName.delete(newName.length() - needSpace, newName.length());
                            }

                            newName.append('#').append(id);
                            field.set(entity, newName.toString());
                        }
                    } catch (Exception e) {
                        this.log.error("error in rename in unique field after soft delete", e);
                    }
                }
            }
        }

    }

    protected void hardDelete(Entity entity) {
        this.log.debug("Request to hard delete Entity : {}", entity.getId());
        this.preDelete(entity, true);
        this.repository.deleteById(entity.getId());
        this.postDelete(entity, true);
    }

    @Transactional
    public void delete(List<Long> ids, boolean isForeHardDelete) {
        this.log.debug("Request to delete Entities : {}", ids);
        ids.forEach((id) -> {
            this.delete(id, isForeHardDelete);
        });
    }

    protected void preDelete(Entity entity, boolean hardDelete) {
    }

    protected void postDelete(Entity entity, boolean hardDelete) {
    }

    protected abstract Class<Entity> getEntityClass();

    @Transactional(readOnly = true)
    public SearchResultModel<Entity> search(SearchDto searchDto, Set<String> entityGraph) {
        this.preSearch(searchDto, false);
        entityGraph = this.entityGraph(entityGraph);
        SearchResultModel<Entity> entityResult = this.repository.search(this.getEntityClass(), searchDto, entityGraph);
        this.postSearch(entityResult, searchDto, false);
        return entityResult;
    }

    @Transactional(readOnly = true)
    public SearchResultModel<Entity> search(SearchDto searchDto) {
        return this.search(searchDto, null);
    }

    protected Set<String> entityGraph(Set<String> entityGraph) {
        return entityGraph;
    }

    @Transactional(readOnly = true)
    public SearchResultModel<Entity> searchDeleted(SearchDto searchDTO) {
        this.preFilter(searchDTO.getFilters(), PreFilterType.SEARCH_DELETED);
        this.preSearch(searchDTO, true);
        SearchResultModel<Entity> entityResult = ((BaseRepository) this.repository).search(this.getEntityClass(), searchDTO);
        this.postSearch(entityResult, searchDTO, true);
        return entityResult;
    }

    protected void postSearch(SearchResultModel<Entity> searchResultModel,
                              SearchDto searchDTO,
                              boolean searchDeleted) {
    }

    protected void preSearch(SearchDto searchDTO, boolean searchDeleted) {
        this.preFilter(searchDTO.getFilters(), PreFilterType.SEARCH);
        searchDTO.addFilter(new FilterDto("deleted", OperatorType.EQ, Boolean.toString(searchDeleted)));
        if (searchDTO.getOrderBy() != null && !searchDTO.getOrderBy().isEmpty()) {
            searchDTO.addOrderBy(new OrderByDto("id", true));
        } else {
            searchDTO.addOrderBy(new OrderByDto("lastModifiedDate", false));
            searchDTO.addOrderBy(new OrderByDto("id", true));
        }

    }

    @Transactional(readOnly = true)
    public Long count(List<FilterDto> filterDTOs) {
        if (filterDTOs == null) {
            filterDTOs = new ArrayList();
        }

        this.preFilter(filterDTOs, PreFilterType.COUNT);
        this.preCountSearch(filterDTOs);
        Long result = (this.repository).count(this.getEntityClass(), filterDTOs);
        this.postCountSearch(result, filterDTOs);
        return result;
    }

    protected void postCountSearch(Long result, List<FilterDto> filterDTOs) {
    }

    protected void preCountSearch(List<FilterDto> filterDTOs) {
    }

    @Transactional(readOnly = true)
    public Number sqlFunction(List<FilterDto> filterDTOs,
                              RaSqlFunction function,
                              String field) {
        if (filterDTOs == null) {
            filterDTOs = new ArrayList();
        }

        this.preFilter(filterDTOs, PreFilterType.FUNCTION);
        this.preSqlFunction(filterDTOs, function, field);
        Number result = (this.repository).sqlFunction(this.getEntityClass(), filterDTOs, function, field);
        this.postSqlFunction(filterDTOs, function, field, result);
        return result;
    }

    protected void postSqlFunction(List<FilterDto> filterDTOs,
                                   RaSqlFunction function,
                                   String field,
                                   Number result) {
    }

    protected void preSqlFunction(List<FilterDto> filterDTOs, RaSqlFunction function, String field) {
    }

    public List<ChartDataModel> groupBy(List<FilterDto> filterDTOs,
                                        String field,
                                        RaSqlFunction function,
                                        String functionField) {
        if (function == null) {
            function = RaSqlFunction.COUNT;
        }

        if (!StringUtils.hasText(functionField)) {
            functionField = "id";
        }

        if (filterDTOs == null) {
            filterDTOs = new ArrayList();
        }

        this.preFilter(filterDTOs, PreFilterType.GROUP_BY);
        this.preGroupBy(filterDTOs, function, field);
        List<ChartDataModel> result = (this.repository).groupBy(this.getEntityClass(),
                filterDTOs, field, function, functionField);
        this.postGroupBy(filterDTOs, function, field, result);
        return result;
    }

    protected void postGroupBy(List<FilterDto> filterDTOs,
                               RaSqlFunction function,
                               String field,
                               List<ChartDataModel> result) {
    }

    protected void preGroupBy(List<FilterDto> filterDTOs,
                              RaSqlFunction function,
                              String field) {
    }

    protected void preFilter(List<FilterDto> filters,
                             PreFilterType filterType) {
    }

    protected void postInitMockData(List<Entity> entities) {
    }

    protected void preInitMockData(List<Entity> entities) {
    }
}
