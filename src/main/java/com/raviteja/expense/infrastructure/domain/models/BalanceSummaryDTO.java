package com.expense.api.infrastructure.domain.models;

import lombok.Data;

@Data
public class BalanceSummaryDTO {
    private String user;
    private Double paid;
    private Double owed;
    private Double netBalance;
}
