package com.api_controle_acesso.services;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilaService {
     private final Queue<Long> queue = new LinkedList<>();
    private final FilaWebsocketService filaWebSocketService;

    @Autowired
    public FilaService(FilaWebsocketService filaWebSocketService) {
        this.filaWebSocketService = filaWebSocketService;
    }

    public void addToQueue(Long userId) {
        queue.add(userId);
        updateQueue();
    }

    public void removeFromQueue(Long userId) {
        queue.remove(userId);
        updateQueue();
    }

    private void updateQueue() {
        int position = 1;
        for (Long userId : queue) {
            filaWebSocketService.updateQueuePosition(userId, position++);
        }
    }

    public void userReturned(Long userId) {
        removeFromQueue(userId);
        notifyNextUser();
    }

    private void notifyNextUser() {
        if (!queue.isEmpty()) {
            Long nextUserId = queue.peek();
            filaWebSocketService.notifyUser(nextUserId, "Você pode sair da sala agora.");
        }
    }
}
