package com.api_controle_acesso.DTOs.UsuarioDTO;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record UsuarioPutDTO(@NotNull Long id, String email, Long curso_id, String nome, @Past LocalDate dataNascimento, String matricula, @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}") String cpf, String nivel) {
    
}
