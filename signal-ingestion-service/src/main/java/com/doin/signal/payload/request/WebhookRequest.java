package com.doin.signal.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest {
    @NotBlank(message = "Signal message is required")
    private String message;

    @NotNull(message = "UserId is required")
    private Long userId;

    private String clientSignalId;
}