package com.api_controle_acesso.models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioPostDTO;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioPutDTO;
import com.api_controle_acesso.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "Usuario")
public class Usuario implements UserDetails {
    
    public Usuario(UsuarioPostDTO dadosUsuario) {
        this.nome = dadosUsuario.nome();
        this.dataNascimento = dadosUsuario.dataNascimento();
        this.matricula = dadosUsuario.matricula();
        this.curso = dadosUsuario.curso();
        this.cpf = dadosUsuario.cpf();
        this.email = dadosUsuario.email();
        this.senha = dadosUsuario.senha();
        this.nivel = dadosUsuario.nivel();
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "matricula")
    private String matricula;

    @JoinColumn(name = "curso_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Curso curso;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Size(max = 100000)
    @Column(name = "foto", columnDefinition = "LONGTEXT")
    private String foto;

    @Column(name = "nivel")
    private String nivel;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return matricula;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void update(@Valid UsuarioPutDTO usuarioPutDTO) {
        if (usuarioPutDTO.email() != null)
            this.email = usuarioPutDTO.email();
        
        if (usuarioPutDTO.nome() != null)
            this.nome = usuarioPutDTO.nome();
        
        if (usuarioPutDTO.dataNascimento() !=  null)
            this.dataNascimento = usuarioPutDTO.dataNascimento();

        if (usuarioPutDTO.matricula() != null)
            this.matricula = usuarioPutDTO.matricula();
        
        if (usuarioPutDTO.cpf() != null)
            this.cpf = usuarioPutDTO.cpf();

        if (usuarioPutDTO.nivel() != null)
            this.nivel = usuarioPutDTO.nivel();
    }
}
