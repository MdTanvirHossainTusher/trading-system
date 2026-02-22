package com.doin.signal.service.impl;

import com.doin.signal.exception.ResourceNotFoundException;
import com.doin.signal.model.BrokerAccount;
import com.doin.signal.model.User;
import com.doin.signal.payload.dto.BrokerAccountDto;
import com.doin.signal.payload.request.BrokerAccountRequest;
import com.doin.signal.repository.BrokerAccountRepository;
import com.doin.signal.repository.UserRepository;
import com.doin.signal.security.ApiKeyEncryptor;
import com.doin.signal.service.BrokerAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerAccountServiceImpl implements BrokerAccountService {

    private final UserRepository userRepository;
    private final BrokerAccountRepository brokerAccountRepository;
    private final ApiKeyEncryptor apiKeyEncryptor;

    @Transactional
    public BrokerAccountDto linkAccount(Long userId, BrokerAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        brokerAccountRepository.findByUserId(userId).stream()
                .filter(BrokerAccount::getActive)
                .forEach(acc -> {
                    acc.setActive(false);
                    brokerAccountRepository.save(acc);
                });

        BrokerAccount account = BrokerAccount.builder()
                .user(user)
                .accountName(request.getAccountName())
                .accountId(request.getAccountId())
                .apiKeyHash(apiKeyEncryptor.encrypt(request.getApiKey()))
                .apiKeyLast4(apiKeyEncryptor.lastFour(request.getApiKey()))
                .brokerType(request.getBrokerType() != null ? request.getBrokerType() : "METATRADER")
                .active(true)
                .build();

        BrokerAccount saved = brokerAccountRepository.save(account);
        log.info("Broker account linked for user {}: {}", userId, saved.getAccountName());
        return toDto(saved);
    }

    public List<BrokerAccountDto> getAccounts(Long userId) {
        return brokerAccountRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private BrokerAccountDto toDto(BrokerAccount acc) {
        return BrokerAccountDto.builder()
                .id(acc.getId())
                .accountName(acc.getAccountName())
                .accountId(acc.getAccountId())
                .apiKeyMasked("****" + acc.getApiKeyLast4())
                .brokerType(acc.getBrokerType())
                .active(acc.getActive())
                .build();
    }
}
