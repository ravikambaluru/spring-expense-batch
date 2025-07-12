package com.raviteja.expense.infrastructure.domain.repository;

import com.raviteja.expense.infrastructure.domain.entity.TransactionEntity;
import com.raviteja.expense.infrastructure.domain.entity.TransactionShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface TransactionShareRepository extends JpaRepository<TransactionShareEntity,Long> {
    List<TransactionShareEntity> findByUser_Id(Long userId);
    TransactionEntity findByUser_IdAndTransaction_Id(Long userId, Long transactionId);

    List<TransactionShareEntity> findByTransaction_Id(Long transactionId);

    @Query("SELECT SUM(ts.shareAmount) FROM TransactionShareEntity ts WHERE ts.user.id = :userId")
    Double getTotalOwedByUser(@Param("userId") Long userId);

    @Query("SELECT ts.user.name AS userName, SUM(ts.shareAmount) AS totalShare FROM TransactionShareEntity ts GROUP BY ts.user.name")
    List<Map<String, Object>> getUserWiseTotalShares();
    @Query("""
    SELECT SUM(ts.shareAmount)
    FROM TransactionShareEntity ts
    WHERE ts.user.id = :userId AND ts.transaction.transactionDate BETWEEN :start AND :end
""")
    Double getTotalOwedByUserForMonth(@Param("userId") Long userId,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end);

    List<TransactionShareEntity> findByUser_IdAndTransaction_TransactionDateBetween(Long userId, LocalDate start, LocalDate end);
    @Modifying
    @Query("DELETE FROM TransactionShareEntity ts WHERE ts.transaction.id = :txnId")
    void deleteByTransactionId(@Param("txnId") Long txnId);

}
