package com.doin.signal.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerAccountRequest {
    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotBlank(message = "API key is required")
    private String apiKey;

    private String brokerType;
}