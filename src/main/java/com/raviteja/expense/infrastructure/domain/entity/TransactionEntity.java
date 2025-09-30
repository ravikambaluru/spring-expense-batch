package com.raviteja.expense.infrastructure.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@Table(name = "transactions")
@Data
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate transactionDate;
    private String transactionMessage;
    private Double transactionAmount;
    @ManyToOne
    private CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "paid_by_id")
    private UserEntity paidBy;
    private Boolean isIncome;
    private Boolean isSharedExpense=false;
    private Boolean canIgnoreTransaction;
    private Boolean isSettlement = false;
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TransactionShareEntity> shares = new ArrayList<>();



    private String notes;
}
