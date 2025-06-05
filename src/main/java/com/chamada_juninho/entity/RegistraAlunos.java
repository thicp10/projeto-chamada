package com.chamada_juninho.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "registra_alunos")
@Builder
public class RegistraAlunos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private Long telefone;

    public RegistraAlunos() {
    }

    public RegistraAlunos(long id, String nome, Long telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }

}

