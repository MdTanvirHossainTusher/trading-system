package com.doin.notification.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RawOrderWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("Raw WS client connected: {}, total sessions: {}", session.getId(), sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Raw WS client disconnected: {}, total sessions: {}", session.getId(), sessions.size());
    }

    public void broadcast(Object message) {
        String payload;
        try {
            payload = new ObjectMapper().writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message: {}", e.getMessage());
            return;
        }

        sessions.removeIf(session -> {
            if (!session.isOpen()) return true;
            try {
                session.sendMessage(new TextMessage(payload));
                return false;
            } catch (IOException e) {
                log.error("Failed to send to session {}: {}", session.getId(), e.getMessage());
                return true;
            }
        });
    }
}