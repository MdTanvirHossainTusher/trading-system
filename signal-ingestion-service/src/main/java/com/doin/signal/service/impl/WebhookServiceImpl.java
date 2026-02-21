package com.doin.signal.service.impl;

import com.doin.signal.exception.ResourceNotFoundException;
import com.doin.signal.model.ActivityLog;
import com.doin.signal.model.BrokerAccount;
import com.doin.signal.payload.dto.RawSignalEvent;
import com.doin.signal.payload.request.WebhookRequest;
import com.doin.signal.payload.response.WebhookResponse;
import com.doin.signal.producer.SignalProducer;
import com.doin.signal.repository.ActivityLogRepository;
import com.doin.signal.repository.BrokerAccountRepository;
import com.doin.signal.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final BrokerAccountRepository brokerAccountRepository;
    private final SignalProducer signalProducer;
    private final ActivityLogRepository activityLogRepository;


    @Override
    public WebhookResponse receiveSignal(Long userId, WebhookRequest request, String ipAddress) {

        BrokerAccount account = brokerAccountRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No active broker account found"));

        String signalId = UUID.randomUUID().toString();

        RawSignalEvent event = RawSignalEvent.builder()
                .signalId(signalId)
                .userId(userId)
                .brokerAccountId(account.getId())
                .rawMessage(request.getMessage())
                .clientSignalId(request.getClientSignalId())
                .receivedAt(LocalDateTime.now().toString())
                .build();

        signalProducer.publishSignal(event);

        log.info("Signal [{}] received from user {} and queued for processing", signalId, userId);

        activityLogRepository.save(ActivityLog.builder()
                .userId(userId)
                .action("SIGNAL_RECEIVED")
                .description("Signal queued: " + request.getMessage().split("\n")[0])
                .ipAddress(ipAddress)
                .build());

        return WebhookResponse.builder()
                .signalId(signalId)
                .status("QUEUED")
                .message("Signal received and queued for processing")
                .build();
    }
}
