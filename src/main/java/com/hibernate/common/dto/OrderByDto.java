package com.hibernate.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderByDto {
    private String name;
    private boolean asc;

    public OrderByDto() {
        this.asc = true;
    }
}