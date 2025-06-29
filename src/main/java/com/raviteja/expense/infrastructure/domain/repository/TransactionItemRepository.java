package com.raviteja.expense.infrastructure.domain.repository;

import com.raviteja.expense.infrastructure.domain.entity.TransItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransItemEntity,Long> {
    List<TransItemEntity> findByTransactionDateBetween(Date startDate, Date endDate);
}
