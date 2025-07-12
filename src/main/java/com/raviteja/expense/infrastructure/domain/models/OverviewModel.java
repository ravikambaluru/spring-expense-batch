package com.raviteja.expense.infrastructure.domain.models;


import com.raviteja.expense.infrastructure.domain.entity.TransactionEntity;

import java.util.List;

public record OverviewModel(Double income, Double expenses, Double remaining, List<TransactionEntity> transactions) {
}
