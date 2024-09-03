package com.api_controle_acesso.DTOs.CursoDTO;
import java.util.List;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioReturnDTO;
import com.api_controle_acesso.models.Curso;

public record CursoReturnGetDTO(Long id, String nome, Integer duracao, List<UsuarioReturnDTO> usuarios) {
    
    public CursoReturnGetDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getDuracao(), curso.getUsuarios().stream().map(UsuarioReturnDTO::new).toList());
    }

    public CursoReturnGetDTO(Curso curso, boolean ignoreUsuariosHorarios) {
        this(curso.getId(), curso.getNome(), curso.getDuracao(), null);
    }
}
