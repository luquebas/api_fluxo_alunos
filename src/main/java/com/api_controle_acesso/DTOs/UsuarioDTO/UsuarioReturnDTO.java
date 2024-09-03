package com.api_controle_acesso.DTOs.UsuarioDTO;
import java.time.LocalDate;
import com.api_controle_acesso.DTOs.CursoDTO.CursoReturnDTO;
import com.api_controle_acesso.models.Usuario;

public record UsuarioReturnDTO(Long id, String nome, LocalDate dataNascimento, String matricula, CursoReturnDTO curso, String cpf, String email, String nivel) {
    
    public UsuarioReturnDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getDataNascimento(), usuario.getMatricula(), new CursoReturnDTO(usuario.getCurso()) , usuario.getCpf(), usuario.getEmail(), usuario.getNivel());
    }
}
