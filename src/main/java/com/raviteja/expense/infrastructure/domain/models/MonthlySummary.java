package com.expense.api.infrastructure.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummary {
    private String month;
    private Double income;
    private Double expense;

}
