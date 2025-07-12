package com.expense.api.infrastructure.domain.models;

public record CategorySplitSettingsRequest(String category, String user, Double percentage) {
}
