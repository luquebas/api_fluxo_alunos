package com.api_controle_acesso.websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.api_controle_acesso.services.FilaService;
import com.api_controle_acesso.services.FilaWebsocketService;

@Component
public class FilaWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private FilaWebsocketService filaWebSocketService;

    @Autowired
    private FilaService filaService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Long userId = extractUserIdFromMessage(payload); // implementar

        if (payload.equals("sair")) {
            filaService.addToQueue(userId);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        filaWebSocketService.addSession(null, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        filaWebSocketService.removeSession(session);
    }

    private Long extractUserIdFromMessage(String message) {
        return Long.parseLong(message);
    }
}
    
