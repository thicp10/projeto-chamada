package com.chamada_juninho.dto;

import com.chamada_juninho.entity.Chamada;
import com.chamada_juninho.entity.Periodo;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChamadaDto {

    private Long id;
    private Long alunoId;
    private String nomeAluno;
    private LocalDate data;
    private Periodo periodo;
    private boolean presente;
    private boolean finalizada;

    public static ChamadaDto fromEntity(Chamada chamada) {
        return ChamadaDto.builder()
                .id(chamada.getId())
                .alunoId(chamada.getAluno().getId())
                .nomeAluno(chamada.getAluno().getNome())
                .data(chamada.getData())
                .periodo(chamada.getPeriodo())
                .presente(chamada.isPresente())
                .finalizada(chamada.isFinalizada())
                .build();
    }
}
