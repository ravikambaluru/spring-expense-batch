package com.raviteja.expense.infrastructure.domain.repository;

import com.raviteja.expense.infrastructure.domain.entity.CategorySplitSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorySplitSettingsRepository extends JpaRepository<CategorySplitSettingsEntity,Long> {
    @Query("select cat from CategorySplitSettingsEntity cat where cat.category.category=:category")
    List<CategorySplitSettingsEntity> findByCategory(@Param("category") String category);
    CategorySplitSettingsEntity findByCategory_IdAndUser_Id(Long categoryId,Long userId);
}
