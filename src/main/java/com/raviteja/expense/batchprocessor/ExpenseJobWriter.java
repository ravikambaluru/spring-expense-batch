package com.raviteja.expense.batchprocessor;
import com.raviteja.expense.infrastructure.domain.entity.TransactionEntity;
import com.raviteja.expense.infrastructure.domain.repository.TransactionItemRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ExpenseJobWriter implements ItemWriter<TransactionEntity> {

    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Override
    public void write(Chunk<? extends TransactionEntity> chunk) {
        System.out.println("========== writing to DB chunk of size" +chunk.size()+" ===========");
        List<TransactionEntity> items = (List<TransactionEntity>) chunk.getItems();
        transactionItemRepository.saveAll(items);
    }
}
