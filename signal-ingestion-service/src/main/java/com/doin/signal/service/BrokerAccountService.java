package com.doin.signal.service;

import com.doin.signal.payload.dto.BrokerAccountDto;
import com.doin.signal.payload.request.BrokerAccountRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface BrokerAccountService {
    BrokerAccountDto linkAccount(Long userId, BrokerAccountRequest request);
    List<BrokerAccountDto> getAccounts(Long userId);
}
