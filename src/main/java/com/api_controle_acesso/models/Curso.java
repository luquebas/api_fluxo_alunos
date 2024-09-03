package com.api_controle_acesso.models;
import java.util.List;
import com.api_controle_acesso.DTOs.CursoDTO.CursoPostDTO;
import com.api_controle_acesso.DTOs.CursoDTO.CursoPutDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Curso")
@Table(name = "curso")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class Curso {
    
    public Curso(CursoPostDTO cursoPostDTO) {
        this.nome = cursoPostDTO.nome();
        this.duracao = cursoPostDTO.duracao();
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "duracao")
    private Integer duracao;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Usuario> usuarios;

        public void update(@Valid CursoPutDTO cursoPutDTO) {
        if (cursoPutDTO.nome() != null)
            this.nome = cursoPutDTO.nome();
        
        if (cursoPutDTO.duracao() != null)
            this.duracao = cursoPutDTO.duracao();
    }
}
