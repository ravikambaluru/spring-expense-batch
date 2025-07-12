package com.expense.api.infrastructure.domain.models;

import lombok.Data;

@Data
public class TransactionBreakdownDTO {

    private Long id;

    private String description;

    private String category;

    private Double amount;

    private String paidBy;

    private Double splitPercentage;

    private Double netImpact; // how much this user owes or is owed

}
