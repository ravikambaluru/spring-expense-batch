package com.expense.api.infrastructure.domain.models;

import lombok.Data;

import java.util.Map;

@Data
public class TransactionRequestDTO {
    private String description;
    private String category;
    private Double amount;
    private Long paidByUserId;
    private Boolean isShared;
    private Boolean isSplitOverridden;
    private Boolean isSettlement;
    private Map<Long, Double> customSplit; // userId -> percentage (only if isSplitOverridden=true)
}
