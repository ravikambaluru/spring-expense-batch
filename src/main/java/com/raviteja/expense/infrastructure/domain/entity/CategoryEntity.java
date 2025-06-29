package com.raviteja.expense.infrastructure.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
}
