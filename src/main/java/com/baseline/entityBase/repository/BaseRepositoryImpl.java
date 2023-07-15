package com.baseline.entityBase.repository;

import com.baseline.common.dto.FilterDto;
import com.baseline.common.dto.OrderByDto;
import com.baseline.common.dto.SearchDto;
import com.baseline.common.enumeration.OperatorType;
import com.baseline.common.enumeration.RaSqlFunction;
import com.baseline.common.model.ChartDataModel;
import com.baseline.entityBase.entity.BaseEntity;
import com.baseline.exception.HttpException;
import com.baseline.common.model.SearchResultModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BaseRepositoryImpl<Entity> implements BaseRepository<Entity> {

    protected final Logger log = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseRepositoryImpl() {
    }

    public SearchResultModel<Entity> search(Class<Entity> entityClass, SearchDto searchDTO) {
        SearchResultModel<Entity> searchResult = new SearchResultModel();
        Long total = -1L;
        if (!searchDTO.isSkipCount()) {
            total = this.count(entityClass, searchDTO.getFilters());
        }

        searchResult.setTotal(total);
        if (searchDTO.isSkipCount() || total != null && total > 0L) {
            CriteriaQuery<Entity> criteria = this.createCriteria(searchDTO, entityClass);
            TypedQuery<Entity> query = this.entityManager.createQuery(criteria);
            if (searchDTO.getSize() != Integer.MAX_VALUE) {
                query.setFirstResult((searchDTO.getPage() - 1) * searchDTO.getSize()).setMaxResults(searchDTO.getSize());
            }

            List<Entity> resultList = query.getResultList();
            searchResult.setResult(resultList);
        } else {
            searchResult.setResult(new ArrayList());
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

        return (Long)this.entityManager.createQuery(criteria).getSingleResult();
    }

    public Number sqlFunction(Class<Entity> entityClass, List<FilterDto> filters, RaSqlFunction function, String field) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteria = builder.createQuery(Number.class);
        Root<Entity> root = criteria.from(entityClass);
        switch (function) {
            case MAX:
                criteria.select(builder.max(root.get(field)));
                break;
            case MIN:
                criteria.select(builder.min(root.get(field)));
                break;
            case COUNT:
                criteria.select(builder.count(root.get(field)));
                break;
            case AVG:
                criteria.select(builder.avg(root.get(field)));
                break;
            case SUM:
                criteria.select(builder.sum(root.get(field)));
                break;
            default:
                throw new IllegalArgumentException("wrong sql function");
        }

        Predicate predicate = this.createPredicates(builder, root, filters, entityClass);
        if (predicate != null) {
            criteria.where(predicate);
        }

        return (Number)this.entityManager.createQuery(criteria).getSingleResult();
    }

    public List<ChartDataModel> groupBy(Class<Entity> entityClass, List<FilterDto> filters, String field, RaSqlFunction function, String functionField) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ChartDataModel> criteria = builder.createQuery(ChartDataModel.class);
        Root<Entity> root = criteria.from(entityClass);
        Path<Comparable<? super Comparable>> expression = this.getExpression(field, root);
        criteria.groupBy(new Expression[]{expression});
        CompoundSelection var10000;
        switch (function) {
            case MAX:
                var10000 = builder.construct(ChartDataModel.class, new Selection[]{expression, builder.max(root.get(functionField))});
                break;
            case MIN:
                var10000 = builder.construct(ChartDataModel.class, new Selection[]{expression, builder.min(root.get(functionField))});
                break;
            case COUNT:
                var10000 = builder.construct(ChartDataModel.class, new Selection[]{expression, builder.count(root.get(functionField))});
                break;
            case AVG:
                var10000 = builder.construct(ChartDataModel.class, new Selection[]{expression, builder.avg(root.get(functionField))});
                break;
            case SUM:
                var10000 = builder.construct(ChartDataModel.class, new Selection[]{expression, builder.sum(root.get(functionField))});
                break;
            default:
                throw new IllegalArgumentException("wrong sql function");
        }

        CompoundSelection<ChartDataModel> construct = var10000;
        criteria.select(construct);
        Predicate predicate = this.createPredicates(builder, root, filters, entityClass);
        if (predicate != null) {
            criteria.where(predicate);
        }

        return this.entityManager.createQuery(criteria).getResultList();
    }

    protected Class<?> preCreatePredicatePerFilter(Class<?> type, FilterDto filter) {
        return type;
    }

    protected Predicate createPredicates(CriteriaBuilder builder, Root<Entity> root, List<FilterDto> filters, Class<Entity> entityClass) {
        if (filters != null && !filters.isEmpty()) {
            List<FilterDto> orFilters = new ArrayList();
            List<FilterDto> andFilters = new ArrayList();
            Iterator var7 = filters.iterator();

            while(var7.hasNext()) {
                FilterDto filterDto = (FilterDto)var7.next();
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
            } else if (orPredicates != null) {
                return orPredicates;
            } else {
                return andPredicates;
            }
        } else {
            return null;
        }
    }

    protected Predicate createPredicate(CriteriaBuilder builder, Root<Entity> root, List<FilterDto> filters, Class<Entity> entityClass, boolean isOr) {
        if (filters != null && !filters.isEmpty()) {
            Predicate result = null;
            Iterator var7 = filters.iterator();

            while(var7.hasNext()) {
                FilterDto filter = (FilterDto)var7.next();
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
                } catch (Exception var22) {
                    throw new HttpException("wrong.field.name.or.function", new String[]{field});
                }

                type = this.preCreatePredicatePerFilter(type, filter);
                Comparable value = null;
                List<Comparable> values = null;
                String[] valuesArray;
                if (!filter.getOperator().equals(OperatorType.IN) && !filter.getOperator().equals(OperatorType.NOT_IN) && !filter.getOperator().equals(OperatorType.BETWEEN)) {
                    value = this.toObject(type, filter.getValue());
                } else {
                    valuesArray = filter.getValue().split(",");
                    values = new ArrayList(valuesArray.length);
                    String[] var16 = valuesArray;
                    int var17 = valuesArray.length;

                    for(int var18 = 0; var18 < var17; ++var18) {
                        String rawValue = var16[var18];
                        Comparable object = this.toObject(type, rawValue);
                        if (object != null) {
                            values.add(object);
                        }
                    }
                }

                Object expression;
                if (isFunction) {
                    try {
                        expression = this.getFunctionExpression(filter.getField(), builder, root);
                    } catch (Exception var21) {
                        throw new HttpException("cannot.parse.function", new String[]{filter.getField()});
                    }
                } else {
                    expression = this.getExpression(filter.getField(), root);
                }

                Predicate predicate;
                switch (filter.getOperator()) {
                    case EQ:
                        predicate = builder.equal((Expression)expression, value);
                        break;
                    case GE:
                        predicate = builder.greaterThanOrEqualTo((Expression)expression, value);
                        break;
                    case GT:
                        predicate = builder.greaterThan((Expression)expression, value);
                        break;
                    case LE:
                        predicate = builder.lessThanOrEqualTo((Expression)expression, value);
                        break;
                    case ILIKE:
                        if (value == null) {
                            throw new HttpException("filter.value.can.not.be.null", new String[]{filter.getValue()});
                        }

                        predicate = builder.like(((Expression)expression).as(String.class), "%" + (String)value + "%");
                        break;
                    case BETWEEN:
                        try {
                            if (values != null && values.size() == 2) {
                                predicate = builder.between((Expression)expression, (Comparable)values.get(0), (Comparable)values.get(1));
                                break;
                            }

                            throw new HttpException("enter.two.value.for.between", new String[]{filter.getValue()});
                        } catch (Exception var23) {
                            throw new HttpException("Illegal.between.value", new String[]{filter.getValue()});
                        }
                    case LIKE:
                        if (value == null) {
                            throw new HttpException("filter.value.can.not.be.null", new String[]{filter.getValue()});
                        }

                        Expression var10001 = builder.lower(((Expression)expression).as(String.class));
                        String var10002 = ((String)value).toLowerCase();
                        predicate = builder.like(var10001, "%" + var10002.replace(' ', '%') + "%");
                        break;
                    case LT:
                        predicate = builder.lessThan((Expression)expression, value);
                        break;
                    case NE:
                        predicate = builder.notEqual((Expression)expression, value);
                        break;
                    case IS_NULL:
                        predicate = builder.isNull((Expression)expression);
                        break;
                    case IS_NOT_NULL:
                        predicate = builder.isNotNull((Expression)expression);
                        break;
                    case IN:
                        predicate = ((Expression)expression).in(values);
                        break;
                    case NOT_IN:
                        predicate = builder.not(((Expression)expression).in(values));
                        break;
                    default:
                        throw new HttpException("Illegal.Operator", new String[]{filter.getOperator().name()});
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
        return cb.function(function.getName(), function.getReturnType(), this.getArgumentExpression(this.getArguments(field), cb, root));
    }

    private Expression[] getArgumentExpression(String[] arguments, CriteriaBuilder cb, Root<Entity> root) {
        List<Expression> expressions = new ArrayList();
        if (arguments != null) {
            String[] var5 = arguments;
            int var6 = arguments.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String argument = var5[var7];
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
        return (Expression[])expressions.toArray(expressionArray);
    }

    private boolean isNumber(String argument) {
        boolean numeric = true;

        try {
            Double var3 = Double.parseDouble(argument);
        } catch (NumberFormatException var4) {
            numeric = false;
        }

        return numeric;
    }

    public String[] getArguments(String function) {
        String substring = function.substring(function.indexOf("(") + 1, function.lastIndexOf(")"));
        String[] arguments = substring.split(",");
        List<String> actualArguments = new ArrayList();

        for(int i = 0; i < arguments.length; ++i) {
            String argument = arguments[i];
            if (!argument.contains("(")) {
                actualArguments.add(argument);
            } else {
                StringBuilder functionArgument = new StringBuilder();
                functionArgument.append(argument).append(",");

                for(int j = i + 1; j < arguments.length; ++j) {
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

        return (String[])actualArguments.toArray(new String[0]);
    }

    private RaSqlFunction getFunction(String field) {
        int indexOfParentheses = field.indexOf("(");
        String functionName = field.substring(0, indexOfParentheses).trim();
        RaSqlFunction[] var4 = RaSqlFunction.values();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            RaSqlFunction value = var4[var6];
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

            for(int i = 1; i < split.length; ++i) {
                expression = ((Path)expression).get(split[i]);
            }
        } else {
            expression = root.get(field);
        }

        return (Path)expression;
    }

    private Class<?> getClassType(String fieldName, Class<Entity> entityClass) {
        Class<?> oldType = entityClass;
        Class<?> currentType = entityClass;
        String[] entityNames = fieldName.split("\\.");
        String[] var6 = entityNames;
        int var7 = entityNames.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String entityName = var6[var8];
            Field field = ReflectionUtils.findField(oldType, entityName);
            if (field == null) {
                throw new HttpException("can.not.found.filed.type", new String[]{fieldName, entityName});
            }

            currentType = field.getType();
            if (currentType == List.class || currentType == Set.class) {
                Type temp = field.getGenericType();
                String className = temp.getTypeName().split("<")[1].substring(0, temp.getTypeName().split("<")[1].length() - 1);

                try {
                    currentType = Class.forName(className);
                } catch (ClassNotFoundException var14) {
                    var14.printStackTrace();
                }
            }

            oldType = currentType;
        }

        return currentType;
    }

    private Comparable toObject(Class clazz, String value) {
        try {
            if (value != null && value.length() != 0) {
                if (Boolean.class != clazz && !clazz.getName().equals("boolean")) {
                    if (Byte.class != clazz && !clazz.getName().equals("byte")) {
                        if (Short.class != clazz && !clazz.getName().equals("short")) {
                            if (Integer.class != clazz && !clazz.getName().equals("int")) {
                                if (Long.class != clazz && !clazz.getName().equals("long")) {
                                    if (Float.class != clazz && !clazz.getName().equals("float")) {
                                        if (Double.class != clazz && !clazz.getName().equals("double")) {
                                            if (clazz.isEnum()) {
                                                return Enum.valueOf(clazz, value);
                                            } else if (Instant.class == clazz) {
                                                Long epochSecond = Long.valueOf(value);
                                                return Instant.ofEpochSecond(epochSecond > 9999999999L ? epochSecond / 1000L : epochSecond);
                                            } else {
                                                if (BaseEntity.class == clazz.getAnnotatedSuperclass().getType()) {
                                                    try {
                                                        return this.toObjectId(clazz, Long.valueOf(value));
                                                    } catch (IllegalAccessException | InstantiationException var4) {
                                                        this.log.error("error.convert.id.to.object", var4);
                                                    }
                                                }

                                                return value;
                                            }
                                        } else {
                                            return Double.parseDouble(value);
                                        }
                                    } else {
                                        return Float.parseFloat(value);
                                    }
                                } else {
                                    return Long.parseLong(value);
                                }
                            } else {
                                return Integer.parseInt(value);
                            }
                        } else {
                            return Short.parseShort(value);
                        }
                    } else {
                        return Byte.parseByte(value);
                    }
                } else {
                    return Boolean.parseBoolean(value);
                }
            } else {
                return value;
            }
        } catch (Exception var5) {
            throw new HttpException("error in convert value '" + value + "' to " + clazz.getName());
        }
    }

    protected Comparable<BaseEntity> toObjectId(Class<BaseEntity> clazz, Long value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BaseEntity entity = (BaseEntity)clazz.getDeclaredConstructor().newInstance();
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
            Iterator var8 = searchDTO.getOrderBy().iterator();

            while(var8.hasNext()) {
                OrderByDto orderByDTO = (OrderByDto)var8.next();
                orders.add(orderByDTO.isAsc() ? builder.asc(this.getExpression(orderByDTO.getName(), root)) : builder.desc(this.getExpression(orderByDTO.getName(), root)));
            }

            criteria.orderBy(orders);
        }

        return criteria;
    }
}
