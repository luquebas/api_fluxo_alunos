package com.api_controle_acesso.DTOs.NotificacaoDTO;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioReturnDTO;
import com.api_controle_acesso.models.Notificacao;

public record NotificacaoReturnDTO(Long id, UsuarioReturnDTO usuario, String mensagem) {

    public NotificacaoReturnDTO(Notificacao notificacao) {
        this(notificacao.getId(), new UsuarioReturnDTO(notificacao.getUsuario()), notificacao.getMensagem());
    }
} 
