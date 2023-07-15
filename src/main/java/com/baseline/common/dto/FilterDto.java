package com.baseline.common.dto;

import com.baseline.common.enumeration.OperatorType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FilterDto {
    private String field;
    private OperatorType operator;
    private String value;
    private boolean or;

    public FilterDto() {
        this.or = false;
        this.value = "";
    }

    public FilterDto(String field, OperatorType operator, String value) {
        this(field, operator);
        this.value = value;
    }

    public FilterDto(String field, OperatorType operator, String value, boolean or) {
        this(field, operator, value);
        this.or = or;
    }

    public FilterDto(String field, OperatorType operator) {
        this();
        this.field = field;
        this.operator = operator;
    }

    protected static SearchFilterDtoBuilder or(SearchDto searchDto) {
        SearchFilterDtoBuilder b = new SearchFilterDtoBuilder(searchDto);
        b.or = true;
        return b;
    }

    protected static SearchFilterDtoBuilder and(SearchDto searchDto) {
        SearchFilterDtoBuilder b = new SearchFilterDtoBuilder(searchDto);
        b.or = false;
        return b;
    }

    public static FilterDtoBuilder or() {
        FilterDtoBuilder b = new FilterDtoBuilder();
        b.or = true;
        return b;
    }

    public static FilterDtoBuilder and() {
        FilterDtoBuilder b = new FilterDtoBuilder();
        b.or = false;
        return b;
    }

    public static class SearchFilterDtoBuilder {
        private String field;
        private boolean or;
        private SearchDto searchDto;

        SearchFilterDtoBuilder(SearchDto searchDto) {
            this.searchDto = searchDto;
        }

        public SearchFilterDtoBuilder field(String field) {
            this.field = field;
            return this;
        }

        public SearchDto eq(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.EQ, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto ne(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.NE, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto ge(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.GE, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto between(String value1, String value2) {
            String value = value1 + ":" + value2;
            FilterDto filterDto = new FilterDto(this.field, OperatorType.BETWEEN, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto gt(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.GT, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto ilike(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.ILIKE, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto le(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.LE, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto lt(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.LT, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto like(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.LIKE, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto in(String value1, String value2, String... values) {
            String finalValue = null;
            StringBuilder sb = new StringBuilder();
            sb.append(value1).append(',').append(value2).append(',');
            if (values != null) {
                String[] var6 = values;
                int var7 = values.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String value = var6[var8];
                    sb.append(value).append(',');
                }
            }

            if (sb.length() > 0) {
                finalValue = sb.deleteCharAt(sb.length() - 1).toString();
            }

            FilterDto filterDto = new FilterDto(this.field, OperatorType.IN, finalValue, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto isNull() {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.IS_NULL, (String)null, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto isNotNull() {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.IS_NOT_NULL, (String)null, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public SearchDto pattern(String value) {
            FilterDto filterDto = new FilterDto(this.field, OperatorType.PATTERN, value, this.or);
            this.searchDto.addFilter(filterDto);
            return this.searchDto;
        }

        public String toString() {
            return "FilterDto.FilterDtoBuilder(field=" + this.field + ", or=" + this.or + ")";
        }
    }
}