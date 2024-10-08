package com.api_controle_acesso.websocket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.api_controle_acesso.repositories.UsuarioRepository;
import com.api_controle_acesso.services.FilaWebsocketService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class FilaWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private FilaWebsocketService filaWebSocketService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    Logger logger = LoggerFactory.getLogger(FilaWebSocketHandler.class);

    public HashMap<Long, Long> pendingRequests = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Long userId = extractUserIdFromMessage(payload);
    
        if (payload.contains("trocarStatus") && userId != null) {
            if (filaWebSocketService.isUserFirstInQueue(userId)) {
                filaWebSocketService.notifyUser(userId, "{\"type\":\"user\",\"status\":\"primeiro\",\"message\":\"Você pode trocar seu status para FORA_DA_SALA.\"}");
                filaWebSocketService.changeUserStatusToForaDaSala(userId);
                filaWebSocketService.notifyUser(userId, "{\"type\":\"user\",\"status\":\"fora_da_sala\"}");
            } else {
                filaWebSocketService.notifyUser(userId, "{\"type\":\"user\",\"status\":\"nao_primeiro\"}");
            }
        } else if (payload.contains("response")) {
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
            Long exitingUserId = jsonObject.get("userId").getAsLong();
            String response = jsonObject.get("response").getAsString();
    
            if (pendingRequests.containsKey(exitingUserId)) {
                if ("sim".equalsIgnoreCase(response)) {
                    filaWebSocketService.approveAddToQueue(exitingUserId);
                    filaWebSocketService.notifyUser(exitingUserId, "{\"type\":\"user\",\"status\":\"authorized\"}");
    
                    if (filaWebSocketService.isUserFirstInQueue(exitingUserId)) {
                        filaWebSocketService.notifyUser(exitingUserId, "{\"type\":\"user\",\"status\":\"primeiro\",\"message\":\"Você é o primeiro na fila e pode trocar seu status para FORA_DA_SALA.\"}");
                    }
                } else if ("não".equalsIgnoreCase(response)) {
                    filaWebSocketService.notifyUser(exitingUserId, "{\"type\":\"user\",\"status\":\"denied\"}");
                }
                pendingRequests.remove(exitingUserId);
            } else {
                session.sendMessage(new TextMessage("Erro: Pedido não encontrado para o usuário."));
            }
        } else {
            if (userId != null) {
                if (payload.contains("sair")) {
                    filaWebSocketService.requestToLeaveRoom(userId, pendingRequests);
                } else if (payload.contains("retornar")) {
                    filaWebSocketService.removeFromQueue(userId);
                }
            } else {
                session.sendMessage(new TextMessage("Erro: ID de usuário não encontrado."));
            }
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
        Long id = null;
        var sess = session.getPrincipal().toString();

        String regex = "Principal=([^,]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sess);

        if (matcher.find()) {
            String email = matcher.group(1);
            id = usuarioRepository.findByEmail(email).getId();
        }

        filaWebSocketService.addSession(id, session);

        List<Long> queue = filaWebSocketService.getQueue();
        int position = queue.indexOf(id) + 1; 

        if (position > 0) {
            filaWebSocketService.notifyUser(id, "{\"type\":\"user\",\"status\":\"posicao_fila\",\"message\":\"Você está na posição " + position + " na fila.\"}");
        } else {
            filaWebSocketService.notifyUser(id, "{\"type\":\"user\",\"status\":\"fora_fila\",\"message\":\"Você não está na fila no momento.\"}");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        filaWebSocketService.removeSession(session);
    }
}
    
