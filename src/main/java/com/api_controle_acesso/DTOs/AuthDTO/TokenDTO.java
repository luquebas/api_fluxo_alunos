package com.api_controle_acesso.DTOs.AuthDTO;

import com.api_controle_acesso.models.enums.Role;

public record TokenDTO(Long id, String nome, String email, String tokenJWT, Role role) {

} 
