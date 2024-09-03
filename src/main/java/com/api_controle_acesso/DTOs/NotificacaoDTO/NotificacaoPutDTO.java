package com.api_controle_acesso.DTOs.NotificacaoDTO;

import jakarta.validation.constraints.NotNull;

public record NotificacaoPutDTO(@NotNull Long id, Long usuario_id, String mensagem ) {
    
}
