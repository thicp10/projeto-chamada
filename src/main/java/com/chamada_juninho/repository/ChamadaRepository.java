package com.chamada_juninho.repository;

import com.chamada_juninho.entity.Chamada;
import com.chamada_juninho.entity.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChamadaRepository extends JpaRepository<Chamada, Long> {

    List<Chamada> findByDataAndPeriodo(LocalDate data, Periodo periodo);

    Optional<Chamada> findByAlunoIdAndDataAndPeriodo(Long alunoId, LocalDate data, Periodo periodo);

    boolean existsByAlunoIdAndDataAndPeriodo(Long alunoId, LocalDate data, Periodo periodo);

    boolean existsByDataAndPeriodoAndFinalizadaTrue(LocalDate data, Periodo periodo);

    List<Chamada> findByData(LocalDate data);

    @Modifying
    @Query("UPDATE Chamada c SET c.finalizada = true WHERE c.data = :data AND c.periodo = :periodo")
    int finalizarPorDataEPeriodo(@Param("data") LocalDate data, @Param("periodo") Periodo periodo);
}
