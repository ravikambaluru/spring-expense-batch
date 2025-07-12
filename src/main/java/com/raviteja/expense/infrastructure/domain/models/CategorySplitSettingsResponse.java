package com.expense.api.infrastructure.domain.models;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategorySplitSettingsResponse(
        Long id,
        String category,
        String user,
        Double percentage,
        LocalDateTime createdAt
) {
}
