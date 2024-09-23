package com.api_controle_acesso.services;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class FilaWebsocketService {

    private final Map<Long, WebSocketSession> userSessions = new HashMap<>();
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final Map<Long, Integer> userQueuePositions = new HashMap<>();
    
    public void addSession(Long userId, WebSocketSession session) {
        userSessions.put(userId, session);
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session2) {
        WebSocketSession session = userSessions.remove(session2);
        sessions.remove(session);
    }

    public void updateQueuePosition(Long userId, int position) {
        userQueuePositions.put(userId, position);
        notifyUser(userId, "Você está na posição " + position + " na fila.");
        if (position == 1) {
            notifyUser(userId, "Você pode sair da sala agora.");
        }
    }

    public void notifyUser(Long userId, String message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyAllSessions(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}