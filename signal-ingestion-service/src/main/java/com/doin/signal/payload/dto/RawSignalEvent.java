package com.doin.signal.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawSignalEvent {
    private String signalId;
    private Long userId;
    private Long brokerAccountId;
    private String rawMessage;
    private String clientSignalId;
    private String receivedAt;
}