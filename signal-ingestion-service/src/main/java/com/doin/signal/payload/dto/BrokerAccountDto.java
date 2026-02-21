package com.doin.signal.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerAccountDto {
    private Long id;
    private String accountName;
    private String accountId;
    private String apiKeyMasked;
    private String brokerType;
    private Boolean active;
}