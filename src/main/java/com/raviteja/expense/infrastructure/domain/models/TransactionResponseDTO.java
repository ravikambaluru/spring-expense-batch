package com.expense.api.infrastructure.domain.models;

import lombok.Data;

import java.util.Map;

@Data
public class TransactionResponseDTO {
    private Long transactionId;
    private String description;
    private Double amount;
    private Map<String, Double> shares;
}
