package com.api_controle_acesso.DTOs.UsuarioDTO;
import java.time.LocalDate;

import com.api_controle_acesso.models.Curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record UsuarioPostDTO(@NotBlank String nome, @Past LocalDate dataNascimento, @NotBlank String matricula, @NotNull Curso curso, @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}") @NotBlank String cpf, @NotBlank String email ,@NotBlank String senha, @NotBlank String nivel) {

}
