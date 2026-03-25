package com.chamada_juninho.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "CHAMADA", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"aluno_id", "data", "periodo"})
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chamada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private RegistraAlunos aluno;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Periodo periodo;

    @Column(nullable = false)
    private boolean presente;

    @Column(nullable = false)
    private boolean finalizada;
}
