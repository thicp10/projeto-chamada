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
    private boolean inativo;
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
    private static String observacao;

    public static RegistraAlunodto fromEntity(RegistraAlunos entity) {
        return RegistraAlunodto.builder()
                .id(entity.getId())
                .inativo(entity.isinativo(observacao))
                .nome(entity.getNome())
                .telefone(entity.getTelefone())
                .matricula(entity.getMatricula())
                .idade(entity.getIdade())
                .declaracao(entity.getDeclaracao())
                .escola(entity.getEscola())
                .periodo(entity.getPeriodo())
                .responsavel(entity.getResponsavel())
                .rgResponsavel(entity.getRgResponsavel())
                .rgCrianca(entity.getRgCrianca())
                .cpfCrianca(entity.getCpfCrianca())
                .build();

    }
    public RegistraAlunos toEntity() {
        return RegistraAlunos.builder()
                .id(this.id)
                .inativo(this.inativo)
                .nome(this.nome)
                .telefone(this.telefone)
                .matricula(getMatricula())
                .idade(this.idade)
                .declaracao(this.declaracao)
                .escola(this.escola)
                .periodo(this.periodo)
                .responsavel(this.responsavel)
                .rgResponsavel(this.rgResponsavel)
                .rgCrianca(this.rgCrianca)
                .cpfCrianca(this.cpfCrianca)
                .build();
    }
}
