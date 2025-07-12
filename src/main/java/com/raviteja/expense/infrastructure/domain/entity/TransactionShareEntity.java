package com.raviteja.expense.infrastructure.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transaction_shares")
@Data
public class TransactionShareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private TransactionEntity transaction;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Double shareAmount;
}

