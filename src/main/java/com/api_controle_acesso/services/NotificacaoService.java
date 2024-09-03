package com.api_controle_acesso.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api_controle_acesso.DTOs.NotificacaoDTO.NotificacaoPostDTO;
import com.api_controle_acesso.DTOs.NotificacaoDTO.NotificacaoReturnDTO;
import com.api_controle_acesso.models.Notificacao;
import com.api_controle_acesso.repositories.NotificacaoRepository;

@Service
public class NotificacaoService {
    
    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioService usuarioService;


    public Notificacao criarNotificacao(NotificacaoPostDTO notificacaoPostDTO) {

        var notificacao = new Notificacao(notificacaoPostDTO);

        var usuario = usuarioService.visualizarUsuario(notificacaoPostDTO.usuario().getId());
        notificacao.setUsuario(usuario);

        return notificacaoRepository.save(notificacao);
    }

    public Page<NotificacaoReturnDTO> visualizarNotificacoes(Pageable pageable) {
        var page = notificacaoRepository.findAll(pageable).map(NotificacaoReturnDTO::new);
        return page;
    }

    public Notificacao visualizarNotificacao(Long id) {
        return notificacaoRepository.getReferenceById(id);
    }

    public void deleteNotificacao(Long id) {
        var horario = notificacaoRepository.getReferenceById(id);
        try {
            notificacaoRepository.delete(horario);
        } catch (Exception e) {
            throw new RuntimeException("Não foi Possível deletar a notificação");
        }
    }

    public List<Notificacao> getUserNotifications(Long userId) {
        return notificacaoRepository.findByUsuarioId(userId);
    }
}
