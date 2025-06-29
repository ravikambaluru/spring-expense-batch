package com.raviteja.expense.batchprocessor;

import java.util.Date;

public record Transaction(
        Date transactionDate,
        String description,
        Double amount
) {
}
