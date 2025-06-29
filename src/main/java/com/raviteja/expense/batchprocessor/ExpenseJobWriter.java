package com.raviteja.expense.batchprocessor;

import com.raviteja.expense.infrastructure.domain.entity.TransItemEntity;
import com.raviteja.expense.infrastructure.domain.repository.TransactionItemRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ExpenseJobWriter implements ItemWriter<TransItemEntity> {

    @Autowired
    private TransactionItemRepository transactionItemRepository;

    @Override
    public void write(Chunk<? extends TransItemEntity> chunk) {
        System.out.println("========== writing to DB chunk of size" +chunk.size()+" ===========");
        List<TransItemEntity> items = (List<TransItemEntity>) chunk.getItems();
        transactionItemRepository.saveAll(items);
    }
}
