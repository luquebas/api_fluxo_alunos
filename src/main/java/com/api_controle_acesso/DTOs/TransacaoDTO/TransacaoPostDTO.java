package com.api_controle_acesso.DTOs.TransacaoDTO;
import java.time.LocalDateTime;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.models.enums.DiaSemana;
import com.api_controle_acesso.models.enums.TipoTransacao;
import jakarta.validation.constraints.NotNull;

public record TransacaoPostDTO(@NotNull Usuario usuario, @NotNull TipoTransacao tipoTransacao, @NotNull LocalDateTime hora, @NotNull DiaSemana diaSemana) {
    
}
