package com.doin.signal.controller;

import com.doin.signal.payload.common.response.ApiResponse;
import com.doin.signal.payload.common.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        return ResponseBuilder.ok(Map.of(
                "service", "signal-ingestion-service",
                "status", "UP"
        ));
    }
}