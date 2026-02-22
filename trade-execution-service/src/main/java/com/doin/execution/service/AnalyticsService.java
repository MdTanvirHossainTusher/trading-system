package com.doin.execution.service;

import com.doin.execution.payload.dto.AnalyticsDto;

public interface AnalyticsService {
    AnalyticsDto getAnalytics(Long userId);
}
