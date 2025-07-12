package com.raviteja.expense.infrastructure.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "category_split_settings")
public class CategorySplitSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category; // e.g., TRAVEL, GROCERIES, etc.

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Double percentage; // e.g., 40.0 for 40%

    private LocalDateTime createdAt;
}


