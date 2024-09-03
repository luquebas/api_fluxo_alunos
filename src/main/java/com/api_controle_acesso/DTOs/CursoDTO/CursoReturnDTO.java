package com.api_controle_acesso.DTOs.CursoDTO;

import com.api_controle_acesso.models.Curso;

public record CursoReturnDTO(Long id, String nome, Integer duracao) {
    public CursoReturnDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getDuracao());
    }
} 
