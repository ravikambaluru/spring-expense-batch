package com.raviteja.expense.infrastructure.domain.models;


import com.raviteja.expense.infrastructure.domain.entity.CategoryEntity;

import java.time.LocalDate;

public record Transaction(Long id, LocalDate transactionDate, String transactionMessage, Double transactionAmount, CategoryEntity category, Boolean isIncome, Boolean isSharedExpense, Boolean canIgnoreTransaction) {
}
