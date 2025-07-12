package com.raviteja.expense.batchprocessor;

import com.raviteja.expense.infrastructure.domain.entity.TransactionEntity;
import com.raviteja.expense.infrastructure.domain.entity.UserEntity;
import com.raviteja.expense.infrastructure.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class ExpenseJobProcessor implements ItemProcessor<Transaction, TransactionEntity> {

    private  final UserRepository userRepository;

    @Value("${application.active.user}")
    private String activeUser;

    @Override
    public TransactionEntity process(Transaction item) throws Exception {
        TransactionEntity transItemEntity = new TransactionEntity();
        UserEntity userEntity = userRepository.findByName(activeUser).orElseThrow();
        transItemEntity.setTransactionAmount(item.amount());
        transItemEntity.setTransactionDate(item.transactionDate());
        String description = item.description();
        transItemEntity.setTransactionMessage(description);
        transItemEntity.setIsIncome(item.isIncome());
        transItemEntity.setIsSharedExpense(item.isSharedExpense());
        transItemEntity.setCanIgnoreTransaction(item.canIgnoreTransaction());
        transItemEntity.setIsSharedExpense(false);
        transItemEntity.setCanIgnoreTransaction(false);
        transItemEntity.setPaidBy(userEntity);
        transItemEntity.setCategory(null);
        return transItemEntity;
    }
}
