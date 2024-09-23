package com.api_controle_acesso.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.repositories.UsuarioRepository;

@Service
public class FilaService {
    private final Queue<Long> queue = new LinkedList<>();
    private final FilaWebsocketService filaWebSocketService;
    private UsuarioRepository usuarioRepository;
    private FilaDeSaidaService filaDeSaidaService;

    @Autowired
    public FilaService(FilaWebsocketService filaWebSocketService, UsuarioRepository usuarioRepository, FilaDeSaidaService filaDeSaidaService) {
        this.filaWebSocketService = filaWebSocketService;
        this.usuarioRepository = usuarioRepository;
        this.filaDeSaidaService = filaDeSaidaService;
    }

    public void addToQueue(Long userId) {
        var u = usuarioRepository.findById(userId);
        Usuario usuario = u.get();
        filaDeSaidaService.adicionarAlunoNaFila(usuario);
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
    }

    public List<Long> getQueue() {
    return new ArrayList<>(queue);
    }
}
