package com.api_controle_acesso.models;
import java.time.LocalDateTime;
import com.api_controle_acesso.DTOs.TransacaoDTO.TransacaoPostDTO;
import com.api_controle_acesso.models.enums.DiaSemana;
import com.api_controle_acesso.models.enums.TipoTransacao;
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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Transacao")
public class Transacao {

    public Transacao(TransacaoPostDTO transacaoPostDTO) {
        this.tipoTransacao = transacaoPostDTO.tipoTransacao();
        this.hora = transacaoPostDTO.hora();
        this.diaSemana = transacaoPostDTO.diaSemana();
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    @Column(name = "tipo_transacao")
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @Column(name = "hora")
    private LocalDateTime hora;

    @Column(name = "dia_da_semana")
    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;
    
}
