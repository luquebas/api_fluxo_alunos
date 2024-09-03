package com.api_controle_acesso.DTOs.NotificacaoDTO;

import com.api_controle_acesso.models.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificacaoPostDTO(@NotNull Usuario usuario, @NotBlank String mensagem) {
    
}
