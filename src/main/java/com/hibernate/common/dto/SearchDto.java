package com.hibernate.common.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchDto {
    private List<FilterDto> filters;
    private int page;
    private int size;
    private List<OrderByDto> orderBy;
    private boolean skipCount;

    public SearchDto() {
        this.filters = new ArrayList();
        this.page = 1;
        this.size = 10;
        this.orderBy = null;
        this.skipCount = false;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        if (page > 0) {
            this.page = page;
        } else {
            this.page = 1;
        }

    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        if (size > 0) {
            this.size = size;
        } else {
            this.size = 1;
        }

    }

    public List<OrderByDto> getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(List<OrderByDto> orderBy) {
        this.orderBy = orderBy;
    }

    public void addOrderBy(OrderByDto orderBy) {
        if (null == this.orderBy) {
            this.orderBy = new ArrayList();
        }

        if (orderBy != null) {
            this.orderBy.add(orderBy);
        }

    }

    public List<FilterDto> getFilters() {
        return this.filters;
    }

    public void setFilters(List<FilterDto> filters) {
        this.filters = filters;
    }

    public void addFilter(FilterDto filter) {
        if (null == this.getFilters()) {
            this.filters = new ArrayList();
        }

        this.filters.add(filter);
    }

    public boolean isSkipCount() {
        return this.skipCount;
    }

    public void setSkipCount(boolean skipCount) {
        this.skipCount = skipCount;
    }

    public void removeIsOr() {
        if (this.filters != null && !this.filters.isEmpty()) {
            this.filters.forEach((f) -> {
                f.setOr(false);
            });
        }
    }

    public FilterDto.SearchFilterDtoBuilder or() {
        return FilterDto.or(this);
    }

    public FilterDto.SearchFilterDtoBuilder and() {
        return FilterDto.and(this);
    }

    public SearchDto asc(String name) {
        OrderByDto orderByDto = new OrderByDto(name, true);
        this.addOrderBy(orderByDto);
        return this;
    }

    public SearchDto desc(String name) {
        OrderByDto orderByDto = new OrderByDto(name, false);
        this.addOrderBy(orderByDto);
        return this;
    }

    public SearchDto page(int page) {
        this.page = page;
        return this;
    }

    public SearchDto size(int size) {
        this.size = size;
        return this;
    }

    public SearchDto skipCount() {
        this.skipCount = true;
        return this;
    }
}