package com.expense.api.infrastructure.domain.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record AnalyticsOverview(Summary summary,
                                Map<String, Double> categoryBreakdown,
                                Map<LocalDate, Double> dailyTrend,
                                Map<String, Double> sharedVsPersonal,
                                List<MonthlySummary> monthlySummary) {
}
