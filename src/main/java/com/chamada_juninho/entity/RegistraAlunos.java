package com.chamada_juninho.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "REGISTRA_ALUNOS")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistraAlunos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Wrapper para permitir null

    private boolean inativo;

    private String nome;

    private LocalDate datadia;

    private String telefone; // Alterado para String

    private String matricula;

    private String curso;

    private Byte idade; // Wrapper para nullable

    private String declaracao;

    private String escola;

    private String periodo;

    private String responsavel;

    private String rgResponsavel;

    private String rgCrianca;

    private String cpfCrianca;

    private String observacao;

    public RegistraAlunos(Long id, String nome, String telefone, String observacao) {
        this.id = id;
        this.observacao = observacao;
        this.inativo = isInativo();
        this.nome = nome;
        this.telefone = telefone;
        this.datadia = null;
        this.matricula = null;
        this.curso = null;
        this.idade = null;
        this.declaracao = null;
        this.escola = null;
        this.periodo = null;
        this.responsavel = null;
        this.rgResponsavel = null;
        this.rgCrianca = null;
        this.cpfCrianca = null;
    }

    public boolean isInativo() {
        return this.observacao != null && this.observacao.toLowerCase().contains("inativo");
    }
}
