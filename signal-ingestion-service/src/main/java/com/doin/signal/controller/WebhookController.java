package com.doin.signal.controller;

import com.doin.signal.payload.common.response.ApiResponse;
import com.doin.signal.payload.common.response.ResponseBuilder;
import com.doin.signal.payload.request.WebhookRequest;
import com.doin.signal.payload.response.WebhookResponse;
import com.doin.signal.service.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/receive-signal")
    public ResponseEntity<ApiResponse<WebhookResponse>> receiveSignal(
            @Valid @RequestBody WebhookRequest request,
            HttpServletRequest httpRequest) {

        Long userId = request.getUserId();
        WebhookResponse response = webhookService.receiveSignal(userId, request, httpRequest.getRemoteAddr());
        return ResponseBuilder.ok(response, "Signal queued for processing");
    }
}
