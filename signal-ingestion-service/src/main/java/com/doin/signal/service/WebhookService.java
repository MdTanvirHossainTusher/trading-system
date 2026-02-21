package com.doin.signal.service;

import com.doin.signal.payload.request.WebhookRequest;
import com.doin.signal.payload.response.WebhookResponse;
import jakarta.validation.Valid;

public interface WebhookService {
    WebhookResponse receiveSignal(Long userId, WebhookRequest request, String remoteAddress);
}
