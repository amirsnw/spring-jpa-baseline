package com.baseline.repository;

import com.baseline.common.dto.FilterDto;
import com.baseline.common.dto.OrderByDto;
import com.baseline.common.dto.SearchDto;
import com.baseline.common.model.ChartDataModel;
import com.baseline.common.model.SearchResultModel;
import com.baseline.config.enumeration.OperatorType;
import com.baseline.config.enumeration.RaSqlFunction;
import com.baseline.entity.BaseEntity;
import com.baseline.exception.CustomException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.*;


@Slf4j
public class BaseRepositoryImpl<Entity> implements BaseRepository<Entity> {

    @PersistenceContext
    protected EntityManager entityManager;

    public SearchResultModel<Entity> search(Class<Entity> entityClass, SearchDto searchDto) {
        return this.search(entityClass, searchDto, null);
    }

    public SearchResultModel<Entity> search(Class<Entity> entityClass, SearchDto searchDto, Set<String> entityGraphList) {
        SearchResultModel<Entity> searchResult = new SearchResultModel<>();
        Long total = -1L;
        if (!searchDto.isSkipCount()) {
            total = this.count(entityClass, searchDto.getFilters());
        }

        // Optimization: first check count
        searchResult.setTotal(total);
        if (!searchDto.isSkipCount() && (total == null || total <= 0L)) {
            searchResult.setResult(new ArrayList<>());
        } else {
            CriteriaQuery<Entity> criteria = this.createCriteria(searchDto, entityClass);
            TypedQuery<Entity> query = this.entityManager.createQuery(criteria);
            if (entityGraphList != null && !entityGraphList.isEmpty()) {
                EntityGraph<Entity> entityGraph = this.entityManager.createEntityGraph(entityClass);
                entityGraph.addAttributeNodes(entityGraphList.toArray(new String[0]));
                query.setHint("jakarta.persistence.fetchgraph", entityGraph);
            }

            if (searchDto.getSize() != Integer.MAX_VALUE) {
                query.setFirstResult(searchDto.getPage() * searchDto.getSize()).setMaxResults(searchDto.getSize());
            }

            List<Entity> resultList = query.getResultList();
            searchResult.setResult(resultList);
        }

        return searchResult;
    }

    public Entity findFirst(Class<Entity> entityClass, SearchDto searchDTO) {
        if (searchDTO == null) {
            searchDTO = new SearchDto();
        }

        searchDTO.setSkipCount(true);
        searchDTO.setSize(1);
        SearchResultModel<Entity> result = this.search(entityClass, searchDTO);
        return result != null && result.getResult() != null && !result.getResult().isEmpty() ? result.getResult().get(0) : null;
    }

    public List<Entity> findAll(Class<Entity> entityClass, SearchDto searchDTO) {
        if (searchDTO == null) {
            searchDTO = new SearchDto();
        }

        searchDTO.setSkipCount(true);
        searchDTO.setSize(Integer.MAX_VALUE);
        return this.search(entityClass, searchDTO).getResult();
    }

    public Long count(Class<Entity> entityClass, List<FilterDto> filters) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Entity> root = criteria.from(entityClass);
        criteria.select(builder.count(root));
        Predicate predicate = this.createPredicates(builder, root, filters, entityClass);
        if (predicate != null) {
            criteria.where(predicate);
        }

        return this.entityManager.createQuery(criteria).getSingleResult();
    }

    public Number sqlFunction(Class<Entity> entityClass, List<FilterDto> filters, RaSqlFunction function, String field) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteria = builder.createQuery(Number.class);
        Root<Entity> root = criteria.from(entityClass);
        switch (function) {
            case MAX -> criteria.select(builder.max(root.get(field)));
            case MIN -> criteria.select(builder.min(root.get(field)));
            case COUNT -> criteria.select(builder.count(root.get(field)));
            case AVG -> criteria.select(builder.avg(root.get(field)));
            case SUM -> criteria.select(builder.sum(root.get(field)));
            case FLOOR -> criteria.select(builder.floor(root.get(field)));
            default -> throw new CustomException("wrong.sql.function");
        }

        Predicate predicate = this.createPredicates(builder, root, filters, entityClass);
        if (predicate != null) {
            criteria.where(predicate);
        }

        return this.entityManager.createQuery(criteria).getSingleResult();
    }

    public List<ChartDataModel> groupBy(Class<Entity> entityClass, List<FilterDto> filters, String field, RaSqlFunction function, String functionField) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ChartDataModel> criteria = builder.createQuery(ChartDataModel.class);
        Root<Entity> root = criteria.from(entityClass);
        Path<Comparable<? super Comparable>> expression = this.getExpression(field, root);
        criteria.groupBy(expression);
        CompoundSelection selection;
        switch (function) {
            case MAX:
                selection = builder.construct(ChartDataModel.class, expression, builder.max(root.get(functionField)));
                break;
            case MIN:
                selection = builder.construct(ChartDataModel.class, expression, builder.min(root.get(functionField)));
                break;
            case COUNT:
                selection = builder.construct(ChartDataModel.class, expression, builder.count(root.get(functionField)));
                break;
            case AVG:
                selection = builder.construct(ChartDataModel.class, expression, builder.avg(root.get(functionField)));
                break;
            case SUM:
                selection = builder.construct(ChartDataModel.class, expression, builder.sum(root.get(functionField)));
                break;
            default:
                throw new IllegalArgumentException("wrong sql function");
        }

        CompoundSelection<ChartDataModel> construct = selection;
        criteria.select(construct);
        Predicate predicate = this.createPredicates(builder, root, filters, entityClass);
        if (predicate != null) {
            criteria.where(predicate);
        }

        return this.entityManager.createQuery(criteria).getResultList();
    }

    protected Predicate createPredicates(CriteriaBuilder builder, Root<Entity> root,
                                         List<FilterDto> filters, Class<Entity> entityClass) {
        if (filters != null && !filters.isEmpty()) {
            List<FilterDto> orFilters = new ArrayList<>();
            List<FilterDto> andFilters = new ArrayList<>();

            for (FilterDto filterDto : filters) {
                if (filterDto.isOr()) {
                    orFilters.add(filterDto);
                } else {
                    andFilters.add(filterDto);
                }
            }

            Predicate orPredicates = this.createPredicate(builder, root, orFilters, entityClass, true);
            Predicate andPredicates = this.createPredicate(builder, root, andFilters, entityClass, false);
            if (orPredicates == null && andPredicates == null) {
                return null;
            } else if (orPredicates != null && andPredicates != null) {
                return builder.and(orPredicates, andPredicates);
            } else return Objects.requireNonNullElse(orPredicates, andPredicates);
        } else {
            return null;
        }
    }

    protected Predicate createPredicate(CriteriaBuilder builder, Root<Entity> root,
                                        List<FilterDto> filters, Class<Entity> entityClass,
                                        boolean isOr) {
        if (filters != null && !filters.isEmpty()) {
            Predicate result = null;

            for (FilterDto filter : filters) {
                String field = filter.getField();
                boolean isFunction = false;

                Class type;
                try {
                    if (filter.getField().contains("(")) {
                        type = this.getFunction(filter.getField()).getReturnType();
                        isFunction = true;
                    } else {
                        type = this.getClassType(field, entityClass);
                    }
                } catch (Exception e) {
                    throw new CustomException("wrong.field.name.or.function");
                }


                Comparable value = null;
                List<Comparable> values = null;
                if (!filter.getOperator().equals(OperatorType.IN)
                        && !filter.getOperator().equals(OperatorType.NOT_IN)
                        && !filter.getOperator().equals(OperatorType.BETWEEN)) {
                    value = this.toObject(type, filter.getValue());
                } else {
                    String[] expressionFilters = filter.getValue().split(",");
                    values = new ArrayList<>(expressionFilters.length);

                    for (String rawValue : expressionFilters) {
                        Comparable object = this.toObject(type, rawValue);
                        if (object != null) {
                            values.add(object);
                        }
                    }
                }

                Expression expression;
                if (isFunction) {
                    try {
                        expression = this.getFunctionExpression(filter.getField(), builder, root);
                    } catch (Exception e) {
                        throw new CustomException("cannot.parse.function");
                    }
                } else {
                    expression = this.getExpression(filter.getField(), root);
                }

                Predicate predicate;
                switch (filter.getOperator()) {
                    case EQ -> predicate = builder.equal(expression, value);
                    case GE -> predicate = builder.greaterThanOrEqualTo(expression, value);
                    case GT -> predicate = builder.greaterThan(expression, value);
                    case LE -> predicate = builder.lessThanOrEqualTo(expression, value);
                    case ILIKE -> {
                        if (value == null) {
                            throw new CustomException("filter.value.can.not.be.null");
                        }
                        predicate = builder.like(expression.as(String.class), "%" + (String) value + "%");
                    }
                    case BETWEEN -> {
                        try {
                            if (values != null && values.size() == 2) {
                                predicate = builder.between(expression, values.get(0), values.get(1));
                                break;
                            }

                            throw new CustomException("enter.two.value.for.between");
                        } catch (Exception e) {
                            throw new CustomException("Illegal.between.value");
                        }
                    }
                    case LIKE -> {
                        if (value == null) {
                            throw new CustomException("filter.value.can.not.be.null");
                        }
                        Expression exp = builder.lower(expression.as(String.class));
                        String v = ((String) value).toLowerCase();
                        predicate = builder.like(exp, "%" + v.replace(' ', '%') + "%");
                    }
                    case LT -> predicate = builder.lessThan(expression, value);
                    case NE -> predicate = builder.notEqual(expression, value);
                    case IS_NULL -> predicate = builder.isNull(expression);
                    case IS_NOT_NULL -> predicate = builder.isNotNull(expression);
                    case IN -> predicate = (expression).in(values);
                    case NOT_IN -> predicate = builder.not((expression).in(values));
                    default -> throw new CustomException("Illegal.Operator");
                }

                if (result == null) {
                    result = predicate;
                } else if (isOr) {
                    result = builder.or(result, predicate);
                } else {
                    result = builder.and(result, predicate);
                }
            }

            return result;
        } else {
            return null;
        }
    }

    private Expression getFunctionExpression(String field, CriteriaBuilder cb, Root<Entity> root) {
        RaSqlFunction function = this.getFunction(field);
        return cb.function(function.getName(), function.getReturnType(),
                this.getArgumentExpression(this.getArguments(field), cb, root));
    }

    private Expression[] getArgumentExpression(String[] arguments, CriteriaBuilder cb, Root<Entity> root) {
        List<Expression> expressions = new ArrayList<>();
        if (arguments != null) {

            for (String argument : arguments) {
                argument = argument.trim();
                if (argument.contains("(")) {
                    expressions.add(this.getFunctionExpression(argument, cb, root));
                } else if (this.isNumber(argument)) {
                    if (argument.contains(".")) {
                        expressions.add(cb.literal(Double.parseDouble(argument)));
                    } else {
                        expressions.add(cb.literal(Long.parseLong(argument)));
                    }
                } else if (argument.contains("'")) {
                    expressions.add(cb.literal(argument.replace("'", "")));
                } else {
                    expressions.add(this.getExpression(argument, root));
                }
            }
        }

        Expression[] expressionArray = new Expression[expressions.size()];
        return expressions.toArray(expressionArray);
    }

    private boolean isNumber(String argument) {
        boolean numeric = true;

        try {
            Double.parseDouble(argument);
        } catch (NumberFormatException var4) {
            numeric = false;
        }

        return numeric;
    }

    public String[] getArguments(String function) {
        String substring = function.substring(function.indexOf("(") + 1, function.lastIndexOf(")"));
        String[] arguments = substring.split(",");
        List<String> actualArguments = new ArrayList<>();

        for (int i = 0; i < arguments.length; ++i) {
            String argument = arguments[i];
            if (!argument.contains("(")) {
                actualArguments.add(argument);
            } else {
                StringBuilder functionArgument = new StringBuilder();
                functionArgument.append(argument).append(",");

                for (int j = i + 1; j < arguments.length; ++j) {
                    String innerArgument = arguments[j];
                    if (innerArgument.contains(")")) {
                        functionArgument.append(innerArgument);
                        i = j;
                        break;
                    }

                    functionArgument.append(innerArgument).append(",");
                }

                actualArguments.add(functionArgument.toString());
            }
        }

        return actualArguments.toArray(new String[0]);
    }

    private RaSqlFunction getFunction(String field) {
        int indexOfParentheses = field.indexOf("(");
        String functionName = field.substring(0, indexOfParentheses).trim();
        RaSqlFunction[] functions = RaSqlFunction.values();

        for (int i = 0; i < functions.length; ++i) {
            RaSqlFunction value = functions[i];
            if (value.getName().equals(functionName)) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }

    private Path<Comparable<? super Comparable>> getExpression(String field, Root<Entity> root) {
        Object expression;
        if (field.contains(".")) {
            String[] split = field.split("\\.");
            expression = root.join(split[0]);

            for (int i = 1; i < split.length; ++i) {
                expression = ((Path) expression).get(split[i]);
            }
        } else {
            expression = root.get(field);
        }

        return (Path) expression;
    }

    private Class<?> getClassType(String fieldName, Class<Entity> entityClass) {
        Class<?> oldType = entityClass;
        Class<?> currentType = entityClass;
        String[] entityNames = fieldName.split("\\.");

        for (int i = 0; i < entityNames.length; ++i) {
            String entityName = entityNames[i];
            Field field = ReflectionUtils.findField(oldType, entityName);
            if (field == null) {
                throw new CustomException("can.not.find.filed.type");
            }

            currentType = field.getType();
            if (currentType == List.class || currentType == Set.class) {
                Type temp = field.getGenericType();
                // String className = temp.getTypeName().split("<")[1].substring(0, temp.getTypeName().split("<")[1].length() - 1);
                String className = temp.getTypeName().substring(1, temp.getTypeName().length() - 1);
                try {
                    currentType = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            oldType = currentType;
        }

        return currentType;
    }

    private Comparable toObject(Class clazz, String value) {
        if (value != null && value.length() != 0) {
            try {
                switch (clazz.getName()) {
                    case "java.lang.Boolean", "boolean" -> {
                        return Boolean.parseBoolean(value);
                    }
                    case "java.lang.Byte", "byte" -> {
                        return Byte.parseByte(value);
                    }
                    case "java.lang.Short", "short" -> {
                        return Short.parseShort(value);
                    }
                    case "java.lang.Integer", "int" -> {
                        return Integer.parseInt(value);
                    }
                    case "java.lang.Long", "long" -> {
                        return Long.parseLong(value);
                    }
                    case "java.lang.Float", "float" -> {
                        return Float.parseFloat(value);
                    }
                    case "java.lang.Double", "double" -> {
                        return Double.parseDouble(value);
                    }
                    default -> {
                        if (clazz.isEnum()) {
                            return Enum.valueOf(clazz, value);
                        } else if (clazz == Instant.class) {
                            Long epochSecond = Long.valueOf(value);
                            return Instant.ofEpochSecond(epochSecond > 9999999999L ? epochSecond / 1000L : epochSecond);
                        } else {
                            if (clazz.getAnnotatedSuperclass().getType() == BaseEntity.class) {
                                try {
                                    return this.toObjectId(clazz, Long.valueOf(value));
                                } catch (IllegalAccessException | InstantiationException var4) {
                                    this.log.error("error.convert.id.to.object", var4);
                                }
                            }
                            return value;
                        }
                    }
                }
            } catch (Exception e) {
                throw new CustomException("error in value convertion '" + value + "' to " + clazz.getName());
            }
        } else {
            return value;
        }
    }

    protected Comparable<BaseEntity> toObjectId(Class<BaseEntity> clazz, Long value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BaseEntity entity = clazz.getDeclaredConstructor().newInstance();
        entity.setId(value);
        return entity;
    }

    private CriteriaQuery<Entity> createCriteria(SearchDto searchDTO, Class<Entity> entityClass) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Entity> criteria = builder.createQuery(entityClass);
        Root<Entity> root = criteria.from(entityClass);
        criteria.select(root);
        Predicate predicate = this.createPredicates(builder, root, searchDTO.getFilters(), entityClass);
        if (predicate != null) {
            criteria.where(predicate);
        }

        if (searchDTO.getOrderBy() != null && !searchDTO.getOrderBy().isEmpty()) {
            List<Order> orders = new ArrayList();
            Iterator iter = searchDTO.getOrderBy().iterator();

            while (iter.hasNext()) {
                OrderByDto orderByDTO = (OrderByDto) iter.next();
                orders.add(orderByDTO.isAsc() ?
                        builder.asc(this.getExpression(orderByDTO.getName(), root))
                        : builder.desc(this.getExpression(orderByDTO.getName(), root)));
            }

            criteria.orderBy(orders);
        }

        return criteria;
    }
}
