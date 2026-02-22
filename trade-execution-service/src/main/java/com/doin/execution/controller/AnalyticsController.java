package com.doin.execution.controller;

import com.doin.execution.payload.dto.AnalyticsDto;
import com.doin.execution.payload.response.ApiResponse;
import com.doin.execution.payload.response.ResponseBuilder;
import com.doin.execution.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<ApiResponse<AnalyticsDto>> getAnalytics(@RequestParam Long userId) {
        return ResponseBuilder.ok(analyticsService.getAnalytics(userId));
    }
}
