package com.raviteja.expense.infrastructure.domain.repository;

import com.raviteja.expense.infrastructure.domain.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionEntity,Long> {
    List<TransactionEntity> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    List<TransactionEntity> findByPaidBy_Id(Long userId);
    List<TransactionEntity> findByPaidBy_IdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);

}
