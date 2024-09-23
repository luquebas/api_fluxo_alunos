package com.api_controle_acesso.websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.api_controle_acesso.services.FilaService;
import com.api_controle_acesso.services.FilaWebsocketService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class FilaWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private FilaWebsocketService filaWebSocketService;

    @Autowired
    private FilaService filaService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
    
        // Supondo que a mensagem está em formato JSON: {"userId": 1}
        Long userId = extractUserIdFromMessage(payload);
    
        if (userId != null) {
            if (payload.contains("sair")) {
                filaService.addToQueue(userId);
            } else if (payload.contains("retornar")) {
                filaService.userReturned(userId);
            }
        } else {
            session.sendMessage(new TextMessage("Erro: ID de usuário não encontrado."));
        }
    }
    
    private Long extractUserIdFromMessage(String message) {
        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            return jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
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
}
    
