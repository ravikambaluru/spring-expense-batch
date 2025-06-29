package com.raviteja.expense.batchprocessor;

import com.raviteja.expense.infrastructure.domain.entity.TransItemEntity;
import org.springframework.batch.item.ItemProcessor;

public class ExpenseJobProcessor implements ItemProcessor<Transaction, TransItemEntity> {
    @Override
    public TransItemEntity process(Transaction item) throws Exception {
        TransItemEntity transItemEntity = new TransItemEntity();
        transItemEntity.setTransactionAmount(item.amount());
        transItemEntity.setTransactionDate(item.transactionDate());
        String description = item.description();
        transItemEntity.setTransactionMessage(description);
        return transItemEntity;
    }
}
