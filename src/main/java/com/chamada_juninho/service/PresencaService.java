package com.chamada_juninho.service;

import com.chamada_juninho.dto.ChamadaDto;
import com.chamada_juninho.dto.ChamadaRegistroDto;
import com.chamada_juninho.entity.Chamada;
import com.chamada_juninho.entity.Periodo;
import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.exception.BusinessException;
import com.chamada_juninho.exception.ResourceNotFoundException;
import com.chamada_juninho.repository.ChamadaRepository;
import com.chamada_juninho.repository.RegistraAlunorepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PresencaService {

    private final RegistraAlunorepository alunoRepository;
    private final ChamadaRepository chamadaRepository;

    public PresencaService(RegistraAlunorepository alunoRepository, ChamadaRepository chamadaRepository) {
        this.alunoRepository = alunoRepository;
        this.chamadaRepository = chamadaRepository;
    }

    public List<RegistraAlunos> listarAlunosAtivos() {
        return alunoRepository.findByInativoFalse();
    }

    @Transactional
    public ChamadaDto registrarPresenca(ChamadaRegistroDto dto) {
        RegistraAlunos aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aluno nao encontrado com o ID: " + dto.getAlunoId()));

        if (aluno.isInativo()) {
            throw new BusinessException(
                    "Aluno '" + aluno.getNome() + "' esta inativo e nao pode ser incluido na chamada");
        }

        if (chamadaRepository.existsByDataAndPeriodoAndFinalizadaTrue(dto.getData(), dto.getPeriodo())) {
            throw new BusinessException(
                    "A chamada para " + dto.getData() + " - " + dto.getPeriodo() + " ja foi finalizada");
        }

        if (chamadaRepository.existsByAlunoIdAndDataAndPeriodo(dto.getAlunoId(), dto.getData(), dto.getPeriodo())) {
            throw new BusinessException(
                    "Ja existe chamada registrada para o aluno ID " + dto.getAlunoId()
                            + " na data " + dto.getData() + " - " + dto.getPeriodo());
        }

        Chamada chamada = Chamada.builder()
                .aluno(aluno)
                .data(dto.getData())
                .periodo(dto.getPeriodo())
                .presente(dto.getPresente())
                .finalizada(false)
                .build();

        Chamada salva = chamadaRepository.save(chamada);
        return ChamadaDto.fromEntity(salva);
    }

    @Transactional
    public ChamadaDto atualizarPresenca(Long chamadaId, boolean presente) {
        Chamada chamada = chamadaRepository.findById(chamadaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Registro de chamada nao encontrado com o ID: " + chamadaId));

        if (chamada.isFinalizada()) {
            throw new BusinessException(
                    "A chamada para " + chamada.getData() + " - " + chamada.getPeriodo()
                            + " ja foi finalizada e nao pode ser editada");
        }

        chamada.setPresente(presente);
        Chamada atualizada = chamadaRepository.save(chamada);
        return ChamadaDto.fromEntity(atualizada);
    }

    public List<ChamadaDto> listarChamadaPorDataEPeriodo(LocalDate data, Periodo periodo) {
        return chamadaRepository.findByDataAndPeriodo(data, periodo)
                .stream()
                .map(ChamadaDto::fromEntity)
                .toList();
    }

    public List<ChamadaDto> listarChamadaPorData(LocalDate data) {
        return chamadaRepository.findByData(data)
                .stream()
                .map(ChamadaDto::fromEntity)
                .toList();
    }

    @Transactional
    public int finalizarChamada(LocalDate data, Periodo periodo) {
        List<Chamada> registros = chamadaRepository.findByDataAndPeriodo(data, periodo);

        if (registros.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Nenhum registro de chamada encontrado para " + data + " - " + periodo);
        }

        if (chamadaRepository.existsByDataAndPeriodoAndFinalizadaTrue(data, periodo)) {
            throw new BusinessException(
                    "A chamada para " + data + " - " + periodo + " ja foi finalizada");
        }

        return chamadaRepository.finalizarPorDataEPeriodo(data, periodo);
    }
}
