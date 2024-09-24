package com.api_controle_acesso.models;

import java.time.LocalDateTime;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fila_de_saida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilaDeSaida {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusFila status;

    @Column(name = "hora_solicitacao")
    private LocalDateTime horaSolicitacao;

    @Column(name = "hora_autorizacao")
    private LocalDateTime horaAutorizacao;

    @Column(name = "autorizado")
    private boolean autorizado;

    public enum StatusFila {
        EM_ESPERA,
        FORA_DA_SALA,
        RETORNOU
    }
}