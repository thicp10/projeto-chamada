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
    private String matricula;
    private byte idade;
    private String declaracao;
    private String escola;
    private String responsavel;
    private String rgResponsavel;
    private String rgCrianca;
    private String cpfCrianca;

    public static RegistraAlunodto fromEntity(RegistraAlunos entity) {
        return RegistraAlunodto.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .telefone(entity.getTelefone())
                .matricula(entity.getMatricula())
                .idade(entity.getIdade())
                .declaracao(entity.getDeclaracao())
                .escola(entity.getEscola())
                .responsavel(entity.getResponsavel())
                .rgResponsavel(entity.getRgResponsavel())
                .rgCrianca(entity.getRgCrianca())
                .cpfCrianca(entity.getCpfCrianca())
                .build();

    }
    public RegistraAlunos toEntity() {
        return RegistraAlunos.builder()
                .id(this.id)
                .nome(this.nome)
                .telefone(this.telefone)
                .matricula(getMatricula())
                .idade(this.idade)
                .declaracao(this.declaracao)
                .escola(this.escola)
                .responsavel(this.responsavel)
                .rgResponsavel(this.rgResponsavel)
                .rgCrianca(this.rgCrianca)
                .cpfCrianca(this.cpfCrianca)
                .build();
    }
}
