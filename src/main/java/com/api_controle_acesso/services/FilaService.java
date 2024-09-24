package com.api_controle_acesso.services;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.repositories.FilaDeSaidaRepository;
import com.api_controle_acesso.repositories.UsuarioRepository;

@Service
public class FilaService {
    private final FilaWebsocketService filaWebSocketService;
    private UsuarioRepository usuarioRepository;
    private FilaDeSaidaService filaDeSaidaService;
    private FilaDeSaidaRepository filaDeSaidaRepository;

    Logger logger = LoggerFactory.getLogger(FilaService.class);

    @Autowired
    public FilaService(FilaWebsocketService filaWebSocketService, UsuarioRepository usuarioRepository, 
                                    FilaDeSaidaService filaDeSaidaService, FilaDeSaidaRepository filaDeSaidaRepository) {
        this.filaWebSocketService = filaWebSocketService;
        this.usuarioRepository = usuarioRepository;
        this.filaDeSaidaService = filaDeSaidaService;
        this.filaDeSaidaRepository = filaDeSaidaRepository;

    }

    public void addToQueue(Long userId) {
        var queue = getQueue();

        var u = usuarioRepository.findById(userId);
        Usuario usuario = u.get();
        filaDeSaidaService.adicionarAlunoNaFila(usuario);

        queue.add(userId);
        updateQueue(queue);
    }

    public void removeFromQueue(Long userId) {
        var queue = getQueue();
        logger.info(userId.toString());

        filaDeSaidaRepository.removerDaLista(userId);
        queue.remove(userId);
        updateQueue(queue);
    }

    private void updateQueue(List<Long> queue) {
        int position = 1;
        for (Long userId : queue) {
            filaWebSocketService.updateQueuePosition(userId, position++);
        }
    }

    public List<Long> getQueue() {
        List<Long> l = filaDeSaidaRepository.verificarAguardando();
        return l;
    }
}
