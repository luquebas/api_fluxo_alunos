package com.api_controle_acesso.DTOs.CursoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoPostDTO(@NotBlank String nome, @NotNull Integer duracao) {
} 
