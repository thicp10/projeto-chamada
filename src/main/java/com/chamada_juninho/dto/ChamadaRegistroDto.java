package com.chamada_juninho.dto;

import com.chamada_juninho.entity.Periodo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChamadaRegistroDto {

    @NotNull(message = "O ID do aluno e obrigatorio")
    private Long alunoId;

    @NotNull(message = "A data e obrigatoria")
    private LocalDate data;

    @NotNull(message = "O periodo e obrigatorio")
    private Periodo periodo;

    @NotNull(message = "O status de presenca e obrigatorio")
    private Boolean presente;
}
