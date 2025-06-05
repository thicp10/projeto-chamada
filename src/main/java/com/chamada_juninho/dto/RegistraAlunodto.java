package com.chamada_juninho.dto;

import com.chamada_juninho.entity.RegistraAlunos;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistraAlunodto {
    private long id;
    private String nome;
    private Long telefone;

    public static RegistraAlunodto fromEntity(RegistraAlunos entity) {
        return RegistraAlunodto.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .telefone(entity.getTelefone())
                .build();
    }

    public RegistraAlunos toEntity() {
        return RegistraAlunos.builder()
                .id(this.id)
                .nome(this.nome)
                .telefone(this.telefone)
                .build();
    }
}
