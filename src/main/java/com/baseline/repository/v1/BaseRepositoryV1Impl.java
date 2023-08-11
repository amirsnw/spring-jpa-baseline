package com.baseline.repository.v1;

import com.baseline.common.model.SearchResultModel;
import com.baseline.repository.v1.dto.Filter;
import com.baseline.repository.v1.dto.FilterWrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class BaseRepositoryV1Impl<ENTITY> implements BaseRepositoryV1<ENTITY> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public SearchResultModel<ENTITY> search(Class<ENTITY> entityClass, FilterWrapper filterWrapper, Integer start, Integer limit) {

        SearchResultModel<ENTITY> searchResult = new SearchResultModel<>();
        long total = getTypedQueryBuilder(entityClass).count();
        List<ENTITY> result = getTypedQueryBuilder(entityClass).filter(filterWrapper).select(start, limit);

        searchResult.setTotal(total);
        searchResult.setResult(result);

        return searchResult;
    }

    private TypedQueryBuilder<ENTITY> getTypedQueryBuilder(Class<ENTITY> entityClass) {
        return new TypedQueryBuilder<>(entityClass, entityManager.getCriteriaBuilder(), entityManager);
    }

    @SuppressWarnings("rawtypes")
    private static class TypedQueryBuilder<ENTITY> {

        private final EntityManager entityManager;

        private final CriteriaBuilder criteriaBuilder;

        private final Class<ENTITY> entityClass;

        private final CriteriaQuery criteriaQuery;

        private final Root<ENTITY> root;

        @SuppressWarnings("unchecked")
        public TypedQueryBuilder(Class<ENTITY> entityClass, CriteriaBuilder criteriaBuilder, EntityManager entityManager) {
            this.entityClass = entityClass;
            this.criteriaBuilder = criteriaBuilder;
            this.entityManager = entityManager;
            criteriaQuery = criteriaBuilder.createQuery();
            root = criteriaQuery.from(entityClass);
        }

        @SuppressWarnings("unchecked")
        public List<ENTITY> select(Integer start, Integer limit) {
            CriteriaQuery<ENTITY> result = criteriaQuery.select(root);
            TypedQuery createQuery = entityManager.createQuery(result);

            List<ENTITY> list = new ArrayList<>();
            if (start != null && limit != null) {
                list = createQuery.setFirstResult(start).setMaxResults(limit).getResultList();
            }
            if (start == null && limit != null) {
                list = createQuery.setMaxResults(limit).getResultList();
            }
            if (start != null && limit == null) {
                list = createQuery.setFirstResult(start).getResultList();
            }
            if (start == null && limit == null) {
                list = createQuery.getResultList();
            }

            return list;
        }

        @SuppressWarnings("unchecked")
        public long count() {
            CriteriaQuery<Long> count = criteriaQuery.select(criteriaBuilder.count(root));
            TypedQuery<Long> createQuery = entityManager.createQuery(count);
            return createQuery.getSingleResult();
        }

        private TypedQueryBuilder<ENTITY> filter(FilterWrapper filterWrapper) {
            List<Predicate> predicates = new ArrayList<>();
            Metamodel metaModel = entityManager.getMetamodel();
            EntityType<ENTITY> entityType = metaModel.entity(entityClass);
            List<Join> innerJoins = new ArrayList<>();

            if (filterWrapper != null && filterWrapper.getFilters() != null) {
                for (Filter filter : filterWrapper.getFilters()) {
                    Object value = filter.getValue();
                    String field = filter.getProperty();

                    String[] split = field.split("\\.");
                    Path path = root.get(split[0]);
                    String name = split[0];
                    for (int j = 1; j < split.length; j++) {
                        if (Collection.class.isAssignableFrom(path.getJavaType())) {
                            innerJoins.add(root.join(name));
                        }
                        path = path.get(split[j]);
                        name = split[j];
                    }

                    switch (filter.getOperator()) {
                        case EQUAL:
                            predicates.add(criteriaBuilder.equal(path, value));
                            break;
                        case LIKE:
                            predicates.add(criteriaBuilder
                                    .like(root.get(entityType.getDeclaredSingularAttribute(field, String.class)),
                                            "%" + value + "%"));
                            break;
                        case AFTER:
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, value.toString()));
                            break;
                        case BEFORE:
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(path, value.toString()));
                            break;
                        default:
                            break;
                    }
                }
            }
            try {
                criteriaQuery.where(predicates.toArray(new Predicate[]{}));
            } catch (Exception e) {
                return this;
            }
            return this;
        }
    }
}
