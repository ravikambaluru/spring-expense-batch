package com.raviteja.expense.infrastructure.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
public class TransItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date transactionDate;
    private String transactionMessage;
    private Double transactionAmount;
    @OneToOne
    private CategoryEntity category;
    private Boolean isIncome;
    private Boolean isSharedExpense;
}
