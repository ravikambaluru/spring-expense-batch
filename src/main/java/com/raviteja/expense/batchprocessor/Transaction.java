package com.raviteja.expense.batchprocessor;

import java.time.LocalDate;

public record Transaction(
        LocalDate transactionDate,
        String description,
        Double amount,
        Boolean isIncome,
        Boolean isSharedExpense,
        Boolean canIgnoreTransaction
) {
}
