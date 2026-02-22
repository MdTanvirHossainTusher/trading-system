package com.doin.signal.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerAccountRequest {

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotBlank(message = "API key is required")
    private String apiKey;

    private String brokerType;
}