package com.api_controle_acesso.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.models.enums.Role;
import com.api_controle_acesso.repositories.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminPassword = "1234";

    private String adminEmail = "admin@gmail.com";

    private String nome = "admin";

    @Override
    public void run(String... args) throws Exception {
        
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Usuario admin = new Usuario();
            admin.setEmail(adminEmail);
            admin.setNome(nome);
            admin.setSenha(passwordEncoder.encode(adminPassword)); 
            admin.setRole(Role.ROLE_ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Usuário administrador criado com sucesso!");
        } else {
            System.out.println("Usuário administrador já existe.");
        }
    }
}
