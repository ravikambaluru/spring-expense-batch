package com.raviteja.expense.infrastructure.domain.repository;

import com.raviteja.expense.infrastructure.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long>
{
}
