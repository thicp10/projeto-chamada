// src/main/java/com/chamada_juninho/dto/RegistraAlunodto.java
package com.chamada_juninho.dto;

import com.chamada_juninho.entity.RegistraAlunos;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistraAlunodto {
    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    private boolean inativo;
    private String telefone;

    @NotBlank(message = "A matrícula não pode estar em branco")
    private String matricula;

    private String curso;

    @NotNull(message = "A data do dia não pode ser nula")
    private LocalDate datadia;

    private Byte idade;

    private String declaracao;

    @NotBlank(message = "A escola não pode estar em branco")
    private String escola;

    @NotBlank(message = "O período não pode estar em branco")
    private String periodo;

    @NotBlank(message = "O nome do responsável não pode estar em branco")
    private String responsavel;

    private String rgResponsavel;
    private String rgCrianca;
    private String cpfCrianca;
    private String observacao;

    public static RegistraAlunodto fromEntity(RegistraAlunos entity) {
        return RegistraAlunodto.builder()
                .id(entity.getId())
                .inativo(entity.isInativo())
                .nome(entity.getNome())
                .telefone(entity.getTelefone())
                .matricula(entity.getMatricula())
                .curso(entity.getCurso())
                .datadia(entity.getDatadia())
                .idade(entity.getIdade())
                .declaracao(entity.getDeclaracao())
                .escola(entity.getEscola())
                .periodo(entity.getPeriodo())
                .responsavel(entity.getResponsavel())
                .rgResponsavel(entity.getRgResponsavel())
                .rgCrianca(entity.getRgCrianca())
                .cpfCrianca(entity.getCpfCrianca())
                .observacao(entity.getObservacao())
                .build();
    }

    public RegistraAlunos toEntity() {
        return RegistraAlunos.builder()
                .id(this.id)
                .inativo(this.inativo)
                .nome(this.nome)
                .telefone(this.telefone)
                .matricula(this.matricula)
                .curso(this.curso)
                .datadia(this.datadia)
                .idade(this.idade)
                .declaracao(this.declaracao)
                .escola(this.escola)
                .periodo(this.periodo)
                .responsavel(this.responsavel)
                .rgResponsavel(this.rgResponsavel)
                .rgCrianca(this.rgCrianca)
                .cpfCrianca(this.cpfCrianca)
                .observacao(this.observacao)
                .build();
    }
}
