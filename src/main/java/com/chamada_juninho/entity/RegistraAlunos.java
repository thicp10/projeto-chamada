package com.chamada_juninho.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private long id;
    private boolean inativo;
    private String nome;
    private Long telefone;
    private String matricula;
    private byte idade;
    private String declaracao;
    private String escola;
    private String periodo;
    private String responsavel;
    private String rgResponsavel;
    private String rgCrianca;
    private String cpfCrianca;
    private String observacao;


    public RegistraAlunos(long id, String nome, Long telefone) {
        this.id = id;
        this.inativo = isinativo(observacao);
        this.nome = nome;
        this.telefone = telefone;
        this.matricula = null;// Inicializa matricula como null
        this.idade =0;
        this.declaracao = null;
        this.escola = null;
        this.periodo = null;
        this.responsavel = null;
        this.rgResponsavel = null;
        this.rgCrianca = null;
        this.cpfCrianca = null;
        this.observacao = null;
    }

    public boolean isinativo(String observacao) {
    return this.observacao != null && this.observacao.toLowerCase().contains("inativo");
    }
}

