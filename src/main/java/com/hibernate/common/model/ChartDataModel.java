package com.hibernate.common.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChartDataModel {
    private Object object;
    private Number number;
}