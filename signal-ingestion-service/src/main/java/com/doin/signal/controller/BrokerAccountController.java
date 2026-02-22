package com.doin.signal.controller;

import com.doin.signal.payload.common.response.ApiResponse;
import com.doin.signal.payload.common.response.ResponseBuilder;
import com.doin.signal.payload.dto.BrokerAccountDto;
import com.doin.signal.payload.request.BrokerAccountRequest;
import com.doin.signal.service.BrokerAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BrokerAccountController {

    private final BrokerAccountService brokerAccountService;

    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<BrokerAccountDto>> linkAccount(
            @Valid @RequestBody BrokerAccountRequest request) {
        Long userId = request.getUserId();
        BrokerAccountDto dto = brokerAccountService.linkAccount(userId, request);
        return ResponseBuilder.created(dto, "Broker account linked successfully");
    }

    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse<List<BrokerAccountDto>>> getAccounts(@RequestParam Long userId) {
        List<BrokerAccountDto> accounts = brokerAccountService.getAccounts(userId);
        return ResponseBuilder.ok(accounts);
    }

}