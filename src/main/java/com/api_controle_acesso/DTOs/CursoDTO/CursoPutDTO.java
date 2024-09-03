package com.api_controle_acesso.DTOs.CursoDTO;
import jakarta.validation.constraints.NotNull;

public record CursoPutDTO(@NotNull Long id, String nome, Integer duracao) {
    
}
