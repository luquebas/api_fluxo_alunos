package com.api_controle_acesso.DTOs.UsuarioDTO;
import java.time.LocalDate;
import com.api_controle_acesso.DTOs.CursoDTO.CursoReturnDTO;
import com.api_controle_acesso.models.Usuario;

public record UsuarioReturnDTO(Long id, String nome, LocalDate dataNascimento, CursoReturnDTO curso, String cpf, String email) {
    
    public UsuarioReturnDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getDataNascimento(), new CursoReturnDTO(usuario.getCurso()) , usuario.getCpf(), usuario.getEmail());
    }
}
