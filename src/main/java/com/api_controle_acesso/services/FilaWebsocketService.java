package com.api_controle_acesso.services;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.api_controle_acesso.models.FilaDeSaida;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.models.enums.Role;
import com.api_controle_acesso.models.enums.TipoSaida;
import com.api_controle_acesso.repositories.FilaDeSaidaRepository;
import com.api_controle_acesso.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Scope("singleton")
public class FilaWebsocketService {

    private final Map<Long, WebSocketSession> userSessions = new HashMap<>();
    private Map<Long, WebSocketSession> sessions = new HashMap<>();
    private final Map<Long, Integer> userQueuePositions = new HashMap<>();

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FilaDeSaidaRepository filaDeSaidaRepository;
    
    Logger logger = LoggerFactory.getLogger(FilaWebsocketService.class);


    public void addSession(Long userId, WebSocketSession session) {
        userSessions.put(userId, session);
        sessions.put(userId, session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    public List<WebSocketSession> getSessions() {
        return new ArrayList<>(sessions.values()); 
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
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void requestToLeaveRoom(Long userId, HashMap<Long, Long> pendingRequests, TipoSaida tipoSaida) {
        Long adminId = getAdminIdForUser(userId);
        pendingRequests.put(userId, adminId);
        notifyAdmin(adminId, userId);
        logger.info("Pedido de saída aguardando aprovação. Usuário: " + userId + " Administrador: " + adminId);
        
        if (isUserFirstInQueue(userId)) {
            notifyUser(userId, "{\"type\":\"user\",\"status\":\"primeiro\",\"message\":\"Você pode trocar seu status para FORA_DA_SALA.\"}");
        }
    }

    private void notifyAdmin(Long adminId, Long userId) {
        WebSocketSession adminSession = userSessions.get(adminId);
        if (adminSession != null && adminSession.isOpen()) {
            String message = "{\"type\":\"admin\",\"userId\":" + userId + ",\"action\":\"authorize\"}";
            try {
                adminSession.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Long getAdminIdForUser(Long userId) {
        return usuarioRepository.findById(userId)
            .filter(user -> user.getCurso() != null)
            .map(user -> usuarioRepository.findByCursoAndRole(user.getCurso(), Role.ROLE_ADMIN))
            .flatMap(admins -> admins.stream().findFirst())
            .map(Usuario::getId).orElse(null);
    }

    public void removeFromQueue(Long userId) {
        filaDeSaidaRepository.removerDaLista(userId);
        updateQueue(getQueue());
    }

    private void updateQueue(List<Long> queue) {
        for (int position = 0; position < queue.size(); position++) {
            updateQueuePosition(queue.get(position), position + 1);
        }
    }

    public boolean isUserFirstInQueue(Long userId) {
        List<Long> queue = getQueue();
        return !queue.isEmpty() && queue.get(0).equals(userId);
    }

    public List<Long> getQueue() {
        return filaDeSaidaRepository.verificarAguardando();
    }

    public void changeUserStatusToForaDaSala(Long userId) {
        filaDeSaidaRepository.findByUsuarioIdAndStatus(userId, FilaDeSaida.StatusFila.EM_ESPERA)
            .ifPresent(fila -> {
                fila.setStatus(FilaDeSaida.StatusFila.FORA_DA_SALA);
                filaDeSaidaRepository.save(fila);
            });
    }

    @Transactional
    public void approveAddToQueue(Long userId, TipoSaida tipoSaida) {
    
        Usuario usuario = usuarioRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        FilaDeSaida filaDeSaida = new FilaDeSaida();
        filaDeSaida.setUsuario(usuario);
        filaDeSaida.setStatus(FilaDeSaida.StatusFila.EM_ESPERA);
        filaDeSaida.setHoraSolicitacao(LocalDateTime.now());
        filaDeSaida.setAutorizado(true);
        filaDeSaida.setTipoSaida(tipoSaida);

        filaDeSaidaRepository.save(filaDeSaida);

        notifyUser(userId, "{\"type\":\"user\",\"status\":\"authorized\"}");

        if (isUserFirstInQueue(userId)) {
            notifyUser(userId, "{\"type\":\"user\",\"status\":\"primeiro\",\"message\":\"Você pode trocar seu status para FORA_DA_SALA.\"}");
        }

        updateQueue(getQueue());
    }

    public void marcarRetorno(Long id) {
        filaDeSaidaRepository.findById(id).ifPresent(filaDeSaida -> {
            filaDeSaida.setStatus(FilaDeSaida.StatusFila.RETORNOU);
            filaDeSaidaRepository.save(filaDeSaida);
        });
    }

    public FilaDeSaida getProximoAluno() {
        return filaDeSaidaRepository.findTopByOrderByHoraSolicitacaoAsc();
    }

    public void autorizarUsuario(Long usuarioId) {
        filaDeSaidaRepository.findByUsuarioIdAndStatus(usuarioId, FilaDeSaida.StatusFila.EM_ESPERA)
        .ifPresent(fila -> {
                fila.setAutorizado(true);
                filaDeSaidaRepository.save(fila);
            });
    }

    public List<FilaDeSaida> getFilaDeSaida() {
        return filaDeSaidaRepository.findByStatusNot(FilaDeSaida.StatusFila.RETORNOU);
    }

}
